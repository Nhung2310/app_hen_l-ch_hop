package com.example.doanthuctap;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doanthuctap.Adapter.MeetingParticipantsAdapter;
import com.example.doanthuctap.Retrofit.Constant;
import com.example.doanthuctap.entity.Meetingparticipants;
import com.example.doanthuctap.restful.MeetingApi;
import com.example.doanthuctap.restful.MeetingparticipantsApi;
import com.example.doanthuctap.util.GsonProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MeetingDetailsActivity extends AppCompatActivity {
    private MeetingparticipantsApi meetingparticipantsApi;
    private MeetingApi meetingApi;

    private TextView locationTextView, dateTextView, start_timeTextView, end_timeTextView, documentsTextView;
    private TextView topicTextView;
    private TextView resultTextView;
    private TextView participantsTextView;
    private ListView participantsListView;
    private Button btnConcludeMeeting; // Khai báo nút "Kết luận cuộc họp"
    private Button btnDownloadDocument;

    private static final int PERMISSION_REQUEST_CODE = 100;
    private String documents;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);

        // Khởi tạo các TextView
        locationTextView = findViewById(R.id.locationTextView);
        topicTextView = findViewById(R.id.topicTextView);
        resultTextView = findViewById(R.id.resultTextView);
        dateTextView = findViewById(R.id.dateTextView);
        start_timeTextView = findViewById(R.id.start_timeTextView);
        end_timeTextView = findViewById(R.id.end_timeTextView);
        documentsTextView = findViewById(R.id.documentsTextView);
        participantsListView = findViewById(R.id.participantsListView);
        btnConcludeMeeting = findViewById(R.id.btnConcludeMeeting); // Khởi tạo nút "Kết luận cuộc họp"

        // Lấy Intent và dữ liệu từ Intent
        Intent intent = getIntent();
        String meetingId = intent.getStringExtra("meeting_id");
        Log.d("MeetingDetailsActivity", "Meeting ID: " + meetingId);
        String meetingDate = intent.getStringExtra("meeting_date");
        String startTime = intent.getStringExtra("start_time");
        String endTime = intent.getStringExtra("end_time");
        String location = intent.getStringExtra("location");
        String agenda = intent.getStringExtra("agenda");
        documents = intent.getStringExtra("documents");
        String result = intent.getStringExtra("result");
        String nextMeetingTime = intent.getStringExtra("next_meeting_time");

        // Cập nhật giao diện với dữ liệu từ Intent
        dateTextView.setText("Ngày: " + meetingDate);
        start_timeTextView.setText("Thời gian bắt đầu: " + startTime);
        end_timeTextView.setText("Thời gian kết thúc: " + endTime);
        locationTextView.setText("Địa điểm: " + location);
        topicTextView.setText("Vấn đề: " + agenda);
        resultTextView.setText("Kết quả phải đạt: " + result);
        documentsTextView.setText("Tài liệu: " + documents);

        // Khởi tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL + "/api/meetingparticipants/meetingparticipants/")
                .addConverterFactory(GsonConverterFactory.create(GsonProvider.getGson()))
                .build();
        meetingparticipantsApi = retrofit.create(MeetingparticipantsApi.class);

        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(Constant.URL + "/api/meeting/meetings/")
                .addConverterFactory(GsonConverterFactory.create(GsonProvider.getGson()))
                .build();
        meetingApi = retrofit1.create(MeetingApi.class);

        // Lấy danh sách người tham gia
        if (meetingId != null) {
            fetchParticipants(meetingId);
        } else {
            Toast.makeText(MeetingDetailsActivity.this, "Không có ID cuộc họp", Toast.LENGTH_SHORT).show();
        }

        // Kiểm tra thời gian kết thúc cuộc họp
        if (endTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime endDateTime = LocalDateTime.parse(meetingDate + " " + endTime, formatter);
            LocalDateTime currentDateTime = LocalDateTime.now();

            if (currentDateTime.isAfter(endDateTime) || currentDateTime.isEqual(endDateTime)) {
                btnConcludeMeeting.setVisibility(View.VISIBLE);
                btnConcludeMeeting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Mở activity hoặc dialog để kết luận cuộc họp
                        showSummaryActivity(v);
                    }
                });
            } else {
                btnConcludeMeeting.setVisibility(View.GONE);
            }
        }

        btnDownloadDocument = findViewById(R.id.btnDownloadDocument);

        // Set click listener for the download button
        btnDownloadDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    downloadDocument();
                } else {
                    requestPermission();
                }
            }
        });
    }

    private void fetchParticipants(String meetingId) {
        meetingparticipantsApi.getParticipantsByMeetingId(meetingId).enqueue(new Callback<List<Meetingparticipants>>() {
            @Override
            public void onResponse(Call<List<Meetingparticipants>> call, Response<List<Meetingparticipants>> response) {
                if (!response.isSuccessful()) {
                    Log.e("MeetingDetailsActivity", "Response Code: " + response.code());
                    Log.e("MeetingDetailsActivity", "Response Message: " + response.message());
                    Toast.makeText(MeetingDetailsActivity.this, "Failed to fetch participants!", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Meetingparticipants> participants = response.body();
                if (participants != null) {
                    Log.d("MeetingDetailsActivity", "Number of participants: " + participants.size());
                    for (Meetingparticipants participant : participants) {
                        Log.d("MeetingDetailsActivity", "Participant ID: " + participant.getParticipantId());
                        Log.d("MeetingDetailsActivity", "Participant Name: " + participant.getParticipantName());
                        Log.d("MeetingDetailsActivity", "Participant Role: " + participant.getRole());
                    }

                    MeetingParticipantsAdapter adapter = new MeetingParticipantsAdapter(MeetingDetailsActivity.this, participants);
                    participantsListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged(); // ensure the adapter refreshes the ListView
                } else {
                    Toast.makeText(MeetingDetailsActivity.this, "Không có người tham gia", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Meetingparticipants>> call, Throwable t) {
                Log.e("MeetingDetailsActivity", "Error: " + t.getMessage());
                Toast.makeText(MeetingDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showEmailDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nhập email để chia sẻ");

        // Thiết lập EditText để nhập email
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setHint("example@gmail.com");
        builder.setView(input);

        // Nút xác nhận
        builder.setPositiveButton("Chia sẻ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = input.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(MeetingDetailsActivity.this, "Email không được để trống", Toast.LENGTH_SHORT).show();
                } else {
                    // Lấy thông tin cuộc họp từ TextView
                    String meetingDate = dateTextView.getText().toString();
                    String startTime = start_timeTextView.getText().toString();
                    String endTime = end_timeTextView.getText().toString();
                    String location = locationTextView.getText().toString();
                    String agenda = topicTextView.getText().toString();
                    String result = resultTextView.getText().toString();
                    String documents = documentsTextView.getText().toString();

                    // Tạo nội dung email
                    String emailSubject = "Thông tin cuộc họp";
                    String emailBody = meetingDate + "\n" +
                            startTime + "\n" +
                            endTime + "\n" +
                            location + "\n" +
                            agenda + "\n" +
                            result + "\n" +
                            documents;

                    // Mở Intent để gửi email
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
                    startActivity(Intent.createChooser(emailIntent, "Chọn ứng dụng email"));
                }
            }
        });

        // Nút hủy
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void showSummaryActivity(View view) {
        Intent intent = null; // Khởi tạo intent với giá trị ban đầu là null
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String role = sharedPreferences.getString("role", null);

        // Kiểm tra vai trò người dùng
        if (role != null) {
            if (role.equals("manager")) {
                intent = new Intent(this, AdminMeetingSummaryActivity.class);
            } else if (role.equals("member")) {
                intent = new Intent(this, MeetingSummaryActivity.class);
            }
        }

        // Kiểm tra xem intent có được khởi tạo không trước khi gọi startActivity
        if (intent != null) {

            intent.putExtra("meeting_id", getIntent().getStringExtra("meeting_id"));
            startActivity(intent);
        } else {
            Toast.makeText(this, "Không thể xác định vai trò người dùng hoặc vai trò không hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MeetingDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MeetingDetailsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            downloadDocument();
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//            } else {
//                Toast.makeText(MeetingDetailsActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
//            }
        }
    }

    private void downloadDocument() {
        String documentUrl = Constant.URL + "/api/meeting/download/" + documents; // Replace with your document URL
        Call<ResponseBody> call = meetingApi.downloadDocument(documentUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body());
                    Toast.makeText(MeetingDetailsActivity.this, "Download " + (writtenToDisk ? "successful" : "failed"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MeetingDetailsActivity.this, "Server returned an error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MeetingDetailsActivity.this, "Download failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        // Use MediaStore API for Android 10 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "document.pdf");
            values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);

            if (uri == null) {
                return false;
            }

            try (InputStream inputStream = body.byteStream();
                 FileOutputStream outputStream = (FileOutputStream) getContentResolver().openOutputStream(uri)) {

                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;

                    Log.d("MeetingDetailsActivity", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            }
        } else {
            // For Android 9 and below
            File futureStudioIconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "document.pdf");

            try (InputStream inputStream = body.byteStream();
                 FileOutputStream outputStream = new FileOutputStream(futureStudioIconFile)) {

                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;

                    Log.d("MeetingDetailsActivity", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }
}
