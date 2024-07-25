package com.example.doanthuctap.Response;

import com.example.doanthuctap.entity.Meeting;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MeetingResponse {
    @SerializedName("meeting_id")
    private int meetingId;

    public MeetingResponse() {
    }

    public MeetingResponse(int meetingId) {
        this.meetingId = meetingId;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }
}
