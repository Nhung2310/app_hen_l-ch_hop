package com.example.doanthuctap.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Summary {
    @SerializedName("summary_id")
    private int summaryId;

    @SerializedName("meeting_id")
    private int meetingId;

    @SerializedName("conclusion")
    private String conclusion;

    @SerializedName("coordinator_id")
    private int coordinatorId;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    public int getSummaryId() {
        return summaryId;
    }

    public void setSummaryId(int summaryId) {
        this.summaryId = summaryId;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public int getCoordinatorId() {
        return coordinatorId;
    }

    public void setCoordinatorId(int coordinatorId) {
        this.coordinatorId = coordinatorId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Summary(int meetingId, String conclusion, int coordinatorId) {
        this.meetingId = meetingId;
        this.conclusion = conclusion;
        this.coordinatorId = coordinatorId;
    }
    // Add this to hold SummaryApproval objects

    @Override
    public String toString() {
        return "Summary{" +
                "summaryId=" + summaryId +
                ", meetingId=" + meetingId +
                ", conclusion='" + conclusion + '\'' +
                ", coordinatorId=" + coordinatorId +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
