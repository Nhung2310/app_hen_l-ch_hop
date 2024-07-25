package com.example.doanthuctap.entity;
import java.sql.Timestamp;
public class Meetingparticipants {



        private int participantId;
        private int meetingId;
        private String participantName;
        private String email;
        private String role; // 'manager' or 'member'
        private int userId;
        private String attendanceStatus;
        private Timestamp createdAt;
        private Timestamp updatedAt;

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
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

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
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
