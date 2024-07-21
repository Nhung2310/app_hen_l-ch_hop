package com.example.doanthuctap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doanthuctap.Adapter.EmployeeSelectionAdapter;
import com.example.doanthuctap.Retrofit.Constant;
import com.example.doanthuctap.entity.Meeting;
import com.example.doanthuctap.entity.User;
import com.example.doanthuctap.restful.MeetingApi;
import com.example.doanthuctap.restful.UserApi;
import com.example.doanthuctap.util.GsonProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
public class CreateMeetingActivity extends AppCompatActivity {

    private EditText uploadLocation, uploadAgenda, uploadResult,  title;
    private Button startTimeButton, endTimeButton, uploadDocsButton, saveButton, meetingDateButton, nextMeetingTimeButton,selectParticipantsButton;
    private TextView selectedFilesTextView,uploadParticipants;
    private int startHour = -1, startMinute = -1, endHour = -1, endMinute = -1;
    private static final int PICK_DOCS_REQUEST_CODE = 1;
    private MeetingApi meetingApi;

    private UserApi userApi;
    private List<User> userList;
    private List<User> selectedUsers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL + "/api/meeting/meetings/")
                .addConverterFactory(GsonConverterFactory.create(GsonProvider.getGson()))
                .build();

        meetingApi = retrofit.create(MeetingApi.class);

        // Initialize Retrofit for User API
        Retrofit userRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL + "/api/user/users/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userApi = userRetrofit.create(UserApi.class);

        // Initialize views
        title = findViewById(R.id.uploadtitle);
        uploadLocation = findViewById(R.id.uploadlocation);
        uploadAgenda = findViewById(R.id.uploadagenda);
        uploadDocsButton = findViewById(R.id.uploadocsButton);
        uploadResult = findViewById(R.id.uploadresult);
        selectParticipantsButton = findViewById(R.id.selectParticipantsButton);

        startTimeButton = findViewById(R.id.starttime);
        endTimeButton = findViewById(R.id.endtime);
        saveButton = findViewById(R.id.savebutton);
        meetingDateButton = findViewById(R.id.meetingdate);
        nextMeetingTimeButton = findViewById(R.id.nextmeetingtime);
        selectedFilesTextView = findViewById(R.id.selectedfilesTextView);
        uploadParticipants = findViewById(R.id.uploadparticipants);



        // Set listeners
        meetingDateButton.setOnClickListener(view -> showDatePickerDialog());
        startTimeButton.setOnClickListener(view -> showTimePickerDialog(true));
        endTimeButton.setOnClickListener(view -> showTimePickerDialog(false));
        uploadDocsButton.setOnClickListener(view -> openFileChooser());
        nextMeetingTimeButton.setOnClickListener(view -> showDateTimePickerDialog());
        saveButton.setOnClickListener(view -> saveData());
        selectParticipantsButton.setOnClickListener(view -> showEmployeeSelectionDialog());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);

                    Calendar today = Calendar.getInstance();
                    if (selectedDate.before(today)) {
                        Toast.makeText(CreateMeetingActivity.this, "Vui lòng chọn ngày hôm nay hoặc tương lai.", Toast.LENGTH_SHORT).show();
                    } else {
                        meetingDateButton.setText(String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear));
                    }
                }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showDateTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);

                    Calendar today = Calendar.getInstance();
                    if (selectedDate.before(today)) {
                        Toast.makeText(CreateMeetingActivity.this, "Vui lòng chọn ngày hôm nay hoặc tương lai.", Toast.LENGTH_SHORT).show();
                    } else {
                        showTimePickerDialogForNextMeeting(selectedYear, selectedMonth, selectedDay);
                    }
                }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePickerDialogForNextMeeting(int year, int month, int day) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, selectedMinute) -> {
                    String nextMeetingTime = String.format("%02d/%02d/%04d %02d:%02d", day, month + 1, year, hourOfDay, selectedMinute);
                    nextMeetingTimeButton.setText(nextMeetingTime);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void showTimePickerDialog(boolean isStartTime) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, selectedMinute) -> {
                    if (isStartTime) {
                        startHour = hourOfDay;
                        startMinute = selectedMinute;
                        startTimeButton.setText(String.format("%02d:%02d", startHour, startMinute));
                    } else {
                        if (startHour == -1) {
                            Toast.makeText(CreateMeetingActivity.this, "Vui lòng chọn thời gian bắt đầu trước.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (hourOfDay < startHour || (hourOfDay == startHour && selectedMinute <= startMinute)) {
                            Toast.makeText(CreateMeetingActivity.this, "Thời gian kết thúc phải sau thời gian bắt đầu.", Toast.LENGTH_SHORT).show();
                        } else {
                            endHour = hourOfDay;
                            endMinute = selectedMinute;
                            endTimeButton.setText(String.format("%02d:%02d", endHour, endMinute));
                        }
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void showEmployeeSelectionDialog() {
        if (userList == null) {
            fetchEmployees();  // Đảm bảo fetchEmployees() khởi tạo userList
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn thành viên tham dự");

        ListView listView = new ListView(this);
        EmployeeSelectionAdapter adapter = new EmployeeSelectionAdapter(this, userList, selectedUsers);
        listView.setAdapter(adapter);

        builder.setView(listView);

        builder.setPositiveButton("OK", (dialog, which) -> {
            StringBuilder participants = new StringBuilder();
            for (User user : selectedUsers) {
                participants.append(user.getFullName()).append(", ");
            }
            uploadParticipants.setText(participants.toString());
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.show();
    }


    private void fetchEmployees() {
        userApi.getAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    Log.e("API Error", "Code: " + response.code() + " Message: " + response.message());
                    Toast.makeText(getApplicationContext(), "Failed to fetch employees!", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<User> users = response.body();
                if (users != null) {
                    // Filter out managers
                    userList = new ArrayList<>();
                    for (User user : users) {
                        Log.d("Employee", "FullName: " + user.getFullName()); // Log giá trị fullName
                        if (!"manager".equals(user.getRole())) {
                            userList.add(user);
                        }
                    }

                    // Gọi hàm để hiển thị danh sách
                    showEmployeeSelectionDialog();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to fetch employees!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"});
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Chọn tài liệu"), PICK_DOCS_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_DOCS_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                ArrayList<Uri> fileUris = new ArrayList<>();
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri fileUri = data.getClipData().getItemAt(i).getUri();
                        fileUris.add(fileUri);
                    }
                } else if (data.getData() != null) {
                    Uri fileUri = data.getData();
                    fileUris.add(fileUri);
                }
                handleSelectedFiles(fileUris);
            }
        }
    }

    private void handleSelectedFiles(ArrayList<Uri> fileUris) {
        StringBuilder fileNames = new StringBuilder("Selected Files:\n");
        for (Uri uri : fileUris) {
            String fileName = getFileName(uri);
            fileNames.append(fileName).append("\n");
        }
        selectedFilesTextView.setText(fileNames.toString());
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void saveData() {
        String meetingTitle = title.getText().toString().trim();
        String meetingLocation = uploadLocation.getText().toString().trim();
        String meetingAgenda = uploadAgenda.getText().toString().trim();
        String meetingResult = uploadResult.getText().toString().trim();
       // String meetingParticipants = .getText().toString().trim();
        String meetingDate = meetingDateButton.getText().toString().trim();
        String startTime = startTimeButton.getText().toString().trim();
        String endTime = endTimeButton.getText().toString().trim();
        String nextMeetingTime = nextMeetingTimeButton.getText().toString().trim();

        if (meetingTitle.isEmpty() || meetingLocation.isEmpty() || meetingAgenda.isEmpty() ||
                meetingResult.isEmpty()  || meetingDate.isEmpty() ||
                startTime.isEmpty() || endTime.isEmpty() || nextMeetingTime.isEmpty()) {
            Toast.makeText(CreateMeetingActivity.this, "Vui lòng điền tất cả các trường.", Toast.LENGTH_SHORT).show();
            return;
        }

        Meeting meeting = new Meeting();
        meeting.setTitle(meetingTitle);
        meeting.setMeetingDate(meetingDate);
        meeting.setLocation(meetingLocation);
        meeting.setAgenda(meetingAgenda);
        meeting.setResult(meetingResult);
        meeting.setStartTime(startTime);
        meeting.setEndTime(endTime);
        meeting.setNextMeetingTime(nextMeetingTime);

        // Gọi API để tạo cuộc họp
        Call<Void> call = meetingApi.createMeeting(meeting);
        call.enqueue(new Callback<Void>() {


            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreateMeetingActivity.this, "Tạo cuộc họp thành công.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String errorBody = "Lỗi không xác định.";
                    try {
                        // Kiểm tra nếu errorBody không phải là null
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e("CreateMeetingActivity", "Lỗi khi đọc phản hồi lỗi: " + e.getMessage());
                        errorBody = "Lỗi khi đọc phản hồi lỗi.";
                    }
                    Log.e("CreateMeetingActivity", "Mã lỗi: " + response.code());
                    Log.e("CreateMeetingActivity", "Nội dung lỗi: " + errorBody);
                    Toast.makeText(CreateMeetingActivity.this, "Lỗi khi tạo cuộc họp: " + errorBody, Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("CreateMeetingActivity", "Lỗi kết nối: " + t.getMessage());
                Toast.makeText(CreateMeetingActivity.this, "Lỗi kết nối.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
