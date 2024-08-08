package com.example.doanthuctap.restful;

import com.example.doanthuctap.entity.SummaryApproval;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SummaryApprovalApi {
    // Endpoint để tạo mới một summary approval
    @POST("/api/summaryapproval/summaryapproval")
    Call<SummaryApproval> createSummaryApproval(@Body SummaryApproval summaryApproval);
}
