package com.example.doanthuctap.Response;

import com.google.gson.annotations.SerializedName;

public class MeetingIdResponse {
    @SerializedName("meeting_id")
    private int meetingId;

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }
}
