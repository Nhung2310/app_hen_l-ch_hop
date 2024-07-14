package com.example.doanthuctap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Meeting {
    private String title;
    private String meetingTime;
    private String location;
    private String agenda;
    private String documents;
    private String result;
    private String nextMeetingTime;
    private String createdAt;
    private String updatedAt;

    public Meeting(String title, String meetingTime) {
        this.title = title;
        this.meetingTime = meetingTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

}
