package com.example.doanthuctap;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.doanthuctap.Adapter.SummaryApprovalAdapter;
import com.example.doanthuctap.Retrofit.Constant;
import com.example.doanthuctap.entity.SummaryApproval;
import com.example.doanthuctap.restful.SummaryApprovalApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailSummaryApprovalActivity extends AppCompatActivity {

    private ListView listViewApprovals;
    private SummaryApprovalAdapter adapter;
    private int summaryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_summary_approval);

        listViewApprovals = findViewById(R.id.listViewApprovals);

        // Nhận summary_id từ Intent
        summaryId = getIntent().getIntExtra("summary_id", -1);

        if (summaryId != -1) {
            fetchSummaryApprovals(summaryId);
        } else {
            Toast.makeText(this, "Không tìm thấy Summary ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchSummaryApprovals(int summaryId) {
        // Tạo một instance của Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL + "/api/summaryapproval/summaryapproval/by-summary-id/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Tạo một instance của SummaryApprovalApi
        SummaryApprovalApi summaryApprovalApi = retrofit.create(SummaryApprovalApi.class);

        // Gọi API để lấy danh sách SummaryApproval
        Call<List<SummaryApproval>> call = summaryApprovalApi.getSummaryApprovalsBySummaryId(summaryId);

        call.enqueue(new Callback<List<SummaryApproval>>() {
            @Override
            public void onResponse(Call<List<SummaryApproval>> call, Response<List<SummaryApproval>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SummaryApproval> approvals = response.body();

                    // Thiết lập adapter với dữ liệu nhận được
                    adapter = new SummaryApprovalAdapter(DetailSummaryApprovalActivity.this, approvals);
                    listViewApprovals.setAdapter(adapter);
                } else {
                    Toast.makeText(DetailSummaryApprovalActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SummaryApproval>> call, Throwable t) {
                Toast.makeText(DetailSummaryApprovalActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
