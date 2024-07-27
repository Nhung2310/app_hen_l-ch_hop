package com.example.doanthuctap.entity;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class Meetingparticipants {

    @SerializedName("participant_id")
    private int participantId; // Chỉnh sửa từ participant_id thành participantId

    @SerializedName("meeting_id")
    private int meetingId;

    @SerializedName("participant_name")
    private String participantName;

    @SerializedName("email")
    private String email;

    @SerializedName("role")
    private String role; // 'manager' or 'member'

    @SerializedName("user_id")
    private int userId;

    @SerializedName("attendance_status")
    private String attendanceStatus; // Chỉnh sửa từ attendance_status thành attendanceStatus

    @SerializedName("created_at")
    private Timestamp createdAt; // Chỉnh sửa từ created_at thành createdAt

    @SerializedName("updated_at")
    private Timestamp updatedAt; // Chỉnh sửa từ updated_at thành updatedAt

    // Constructors
    public Meetingparticipants() {
    }

    public int getParticipantId() { // Chỉnh sửa từ getParticipant_id thành getParticipantId
        return participantId;
    }

    public void setParticipantId(int participantId) { // Chỉnh sửa từ setParticipant_id thành setParticipantId
        this.participantId = participantId;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAttendanceStatus() { // Chỉnh sửa từ getAttendance_status thành getAttendanceStatus
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) { // Chỉnh sửa từ setAttendance_status thành setAttendanceStatus
        this.attendanceStatus = attendanceStatus;
    }

    public Timestamp getCreatedAt() { // Chỉnh sửa từ getCreated_at thành getCreatedAt
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) { // Chỉnh sửa từ setCreated_at thành setCreatedAt
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() { // Chỉnh sửa từ getUpdated_at thành getUpdatedAt
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) { // Chỉnh sửa từ setUpdated_at thành setUpdatedAt
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Meetingparticipants{" +
                "participantId=" + participantId +
                ", meetingId=" + meetingId +
                ", participantName='" + participantName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", userId=" + userId +
                ", attendanceStatus='" + attendanceStatus + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
