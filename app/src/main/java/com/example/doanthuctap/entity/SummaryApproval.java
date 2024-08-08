package com.example.doanthuctap.entity;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class SummaryApproval {
    @SerializedName("approval_id")
    private int approvalId;

    @SerializedName("summary_id")
    private int summaryId;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("approval_status")
    private String approvalStatus;

    @SerializedName("review_suggestion")
    private String reviewSuggestion;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    public int getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(int approvalId) {
        this.approvalId = approvalId;
    }

    public int getSummaryId() {
        return summaryId;
    }

    public void setSummaryId(int summaryId) {
        this.summaryId = summaryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getReviewSuggestion() {
        return reviewSuggestion;
    }

    public void setReviewSuggestion(String reviewSuggestion) {
        this.reviewSuggestion = reviewSuggestion;
    }


    public SummaryApproval(int summaryId, int userId, String approvalStatus, String reviewSuggestion) {
        this.summaryId = summaryId;
        this.userId = userId;
        this.approvalStatus = approvalStatus;
        this.reviewSuggestion = reviewSuggestion;
    }

    @Override
    public String toString() {
        return "SummaryApproval{" +
                "approvalId=" + approvalId +
                ", summaryId=" + summaryId +
                ", userId=" + userId +
                ", approvalStatus='" + approvalStatus + '\'' +
                ", reviewSuggestion='" + reviewSuggestion + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
