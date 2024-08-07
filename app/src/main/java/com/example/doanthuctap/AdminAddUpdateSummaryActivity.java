package com.example.doanthuctap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doanthuctap.Retrofit.Constant;
import com.example.doanthuctap.entity.Summary;
import com.example.doanthuctap.restful.MeetingApi;
import com.example.doanthuctap.restful.SummariesApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminAddUpdateSummaryActivity extends AppCompatActivity {

    private SummariesApi summariesApi;
    private EditText etMeetingId, etConclusion, etCoordinatorId;
    private Button btnSave, btnCancel;
    private String meetingId;
    private int coordinatorId; // Thay đổi kiểu dữ liệu để phù hợp với user_id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_update_summary);


        etConclusion = findViewById(R.id.etConclusion);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
// Nhận meetingId từ Intent
        Intent intent = getIntent();
        meetingId = intent.getStringExtra("meeting_id");

        // Lấy coordinatorId từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        coordinatorId = sharedPreferences.getInt("user_id", -1); // -1 là giá trị mặc định nếu không tìm thấy


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL + "/api/summaries/summaries/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        summariesApi = retrofit.create(SummariesApi.class);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              saveSummary();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveSummary() {
        String conclusion = etConclusion.getText().toString();
        int meetingIdInt;

        try {
            meetingIdInt = Integer.parseInt(meetingId);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid meeting ID format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (coordinatorId == -1) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
            return;
        }

        Summary summary = new Summary(meetingIdInt, conclusion, coordinatorId);

        Call<Void> call = summariesApi.addSummary(summary);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Trả kết quả cho hoạt động gọi
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent); // Gửi kết quả thành công
                    finish();
                } else {
                    Toast.makeText(AdminAddUpdateSummaryActivity.this, "Failed to save summary", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AdminAddUpdateSummaryActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}