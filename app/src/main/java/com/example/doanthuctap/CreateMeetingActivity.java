package com.example.doanthuctap;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateMeetingActivity extends AppCompatActivity {

    private TextView selectedFilesTextView;

    ImageView uploadImage;
    Button saveButton, startTimeButton, endTimeButton;
    private int startHour, startMinute, endHour, endMinute;
    private static final int PICK_DOCS_REQUEST_CODE = 1;
    Button uploadDocsButton;
    EditText uploadLocation, uploadTopic,  uploadResult, uploadParticipants;
    Uri uri;
    String imagePath;


    Button uploadTimeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);
        uploadImage = findViewById(R.id.uploadImage);
        uploadLocation = findViewById(R.id.uploadLocation);
        uploadTopic = findViewById(R.id.uploadTopic);
        uploadDocsButton = findViewById(R.id.uploadDocsButton);
        uploadResult = findViewById(R.id.uploadResult);
        uploadParticipants = findViewById(R.id.uploadParticipants);
        startTimeButton = findViewById(R.id.startTimeButton);
        endTimeButton = findViewById(R.id.endTimeButton);
        saveButton = findViewById(R.id.saveButton);

        uploadTimeButton = findViewById(R.id.uploadTimeButton);
// chọn ngày
        uploadTimeButton.setOnClickListener(view -> showDatePickerDialog());
// chọn thời gian bắt đầu vaf kết thúc
        startTimeButton.setOnClickListener(view -> showTimePickerDialog(true));
        endTimeButton.setOnClickListener(view -> showTimePickerDialog(false));
         // upload tài liệu lên
        uploadDocsButton = findViewById(R.id.uploadDocsButton);
        selectedFilesTextView = findViewById(R.id.selectedFilesTextView);

        uploadDocsButton.setOnClickListener(view -> openFileChooser());

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            uploadImage.setImageURI(uri);
                        } else {
                            Toast.makeText(CreateMeetingActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);

                    Calendar today = Calendar.getInstance();
                    if (selectedDate.before(today)) {
                        Toast.makeText(CreateMeetingActivity.this, "Vui lòng chọn ngày hôm nay hoặc tương lai.", Toast.LENGTH_SHORT).show();
                    } else {
                        uploadTimeButton.setText(String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear));
                    }
                }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
    private void showTimePickerDialog(boolean isStartTime) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (TimePicker view, int hourOfDay, int selectedMinute) -> {
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
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
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
    public void saveData() {
        if (uri == null) {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            File file = new File(getExternalFilesDir(null), System.currentTimeMillis() + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            imagePath = file.getAbsolutePath();

            uploadData();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadData() {
        String location = uploadLocation.getText().toString();
        String topic = uploadTopic.getText().toString();
        String docs =uploadDocsButton.getText().toString();
        String result = uploadResult.getText().toString();
        String participants = uploadParticipants.getText().toString();
        // Hiển thị thông báo khi dữ liệu được lưu thành công

    }
}

