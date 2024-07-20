package com.example.doanthuctap.restful;

import com.example.doanthuctap.entity.Meeting;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MeetingApi {
    @GET("/api/meeting/meetings")
    Call<List<Meeting>> getAllMeetings();

}
