package com.example.doanthuctap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.doanthuctap.Adapter.AdminSummaryApprovalAdapter;
import com.example.doanthuctap.entity.SummaryApproval;
import com.example.doanthuctap.restful.MeetingApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminSummaryApprovalActivity extends AppCompatActivity {

    private MeetingApi meetingApi;
    private RecyclerView recyclerViewApprovals;
    private AdminSummaryApprovalAdapter approvalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_summary_approval);

        recyclerViewApprovals = findViewById(R.id.recyclerViewApprovals);
        recyclerViewApprovals.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://yourapiurl.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        meetingApi = retrofit.create(MeetingApi.class);

      //  fetchApprovals();
    }

//    private void fetchApprovals() {
//        Call<List<SummaryApproval>> call = meetingApi.getAllSummaryApprovals();
//        call.enqueue(new Callback<List<SummaryApproval>>() {
//            @Override
//            public void onResponse(Call<List<SummaryApproval>> call, Response<List<SummaryApproval>> response) {
//                if (response.isSuccessful()) {
//                    List<SummaryApproval> approvals = response.body();
//                    approvalAdapter = new AdminSummaryApprovalAdapter(approvals);
//                    recyclerViewApprovals.setAdapter(approvalAdapter);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<SummaryApproval>> call, Throwable t) {
//                Toast.makeText(AdminSummaryApprovalActivity.this, "Failed to fetch approvals", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}