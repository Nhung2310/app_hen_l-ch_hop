package com.example.doanthuctap.restful;

import com.example.doanthuctap.entity.SummaryApproval;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SummaryApprovalApi {
    // Endpoint để tạo mới một summary approval
    @POST("/api/summaryapproval/summaryapproval")
    Call<SummaryApproval> createSummaryApproval(@Body SummaryApproval summaryApproval);

    // Endpoint để lấy danh sách summary approval theo summary_id
    @GET("/api/summaryapproval/summaryapproval/by-summary-id/{summaryId}")
    Call<List<SummaryApproval>> getSummaryApprovalsBySummaryId(@Path("summaryId") int summaryId);

}
