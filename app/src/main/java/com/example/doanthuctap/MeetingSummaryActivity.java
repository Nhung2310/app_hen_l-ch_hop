package com.example.doanthuctap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanthuctap.Adapter.AdminSummaryAdapter;
import com.example.doanthuctap.Adapter.MemberSummaryAdapter;
import com.example.doanthuctap.Retrofit.Constant;
import com.example.doanthuctap.entity.Summary;
import com.example.doanthuctap.restful.SummariesApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MeetingSummaryActivity extends AppCompatActivity {

    private Button btnSave;
    private static final int REQUEST_CODE_ADD_SUMMARY = 1; // Mã yêu cầu cho Activity kết quả
    private TextView tvNoSummaries;
    private SummariesApi summariesApi;
    private RecyclerView recyclerViewSummaries;
    private MemberSummaryAdapter summaryAdapter;

    private String meetingId;

    private String userId; // ID của người dùng hiện tại
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_summary);

        recyclerViewSummaries = findViewById(R.id.recyclerViewSummaries);
        recyclerViewSummaries.setLayoutManager(new LinearLayoutManager(this));
        tvNoSummaries = findViewById(R.id.tvNoSummaries);


        // Lấy meetingId từ Intent
        meetingId = getIntent().getStringExtra("meeting_id");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL + "/api/summaries/summaries-by-meeting-id/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        summariesApi = retrofit.create(SummariesApi.class);

        if (meetingId != null) {
            fetchSummaries(meetingId);
        } else {
            Toast.makeText(MeetingSummaryActivity.this, "Không có ID cuộc họp", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_SUMMARY && resultCode == RESULT_OK) {
            // Làm mới dữ liệu khi kết luận được thêm hoặc cập nhật
            fetchSummaries(meetingId);
        }
    }

    private void fetchSummaries(String meetingId) {
        Call<List<Summary>> call = summariesApi.getSummariesByMeetingId(meetingId);
        call.enqueue(new Callback<List<Summary>>() {
            @Override
            public void onResponse(Call<List<Summary>> call, Response<List<Summary>> response) {
                if (response.isSuccessful()) {
                    List<Summary> summaries = response.body();
                    if (summaries != null && !summaries.isEmpty()) {
                        summaryAdapter = new MemberSummaryAdapter(summaries);
                        recyclerViewSummaries.setAdapter(summaryAdapter);
                        tvNoSummaries.setVisibility(View.GONE); // Ẩn thông báo nếu có kết luận
                    } else {
                        recyclerViewSummaries.setAdapter(null);
                        tvNoSummaries.setVisibility(View.VISIBLE); // Hiện thông báo nếu không có kết luận
                    }
                } else {
                    tvNoSummaries.setVisibility(View.VISIBLE);
                    Toast.makeText(MeetingSummaryActivity.this, "Failed to fetch summaries: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Summary>> call, Throwable t) {
                Toast.makeText(MeetingSummaryActivity.this, "Failed to fetch summaries: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
