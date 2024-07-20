package com.example.doanthuctap.entity;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

public class Meeting {
    @SerializedName("meeting_id")
    private int meetingId;
    @SerializedName("title")
    private String title;
    @SerializedName("meeting_date")
    private String  meetingDate;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("location")
    private String location;
    @SerializedName("agenda")
    private String agenda;
    @SerializedName("documents")
    private String documents;
    @SerializedName("result")
    private String result;
    @SerializedName("next_meeting_time")
    private String nextMeetingTime;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    public Meeting() {
    }

    public Meeting(int meetingId, String title, String  meetingDate, String startTime, String endTime, String location, String agenda, String documents, String result, String nextMeetingTime, String createdAt, String updatedAt) {
        this.meetingId = meetingId;
        this.title = title;
        this.meetingDate = meetingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.agenda = agenda;
        this.documents = documents;
        this.result = result;
        this.nextMeetingTime = nextMeetingTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String  getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String  meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getNextMeetingTime() {
        return nextMeetingTime;
    }

    public void setNextMeetingTime(String nextMeetingTime) {
        this.nextMeetingTime = nextMeetingTime;
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
}
