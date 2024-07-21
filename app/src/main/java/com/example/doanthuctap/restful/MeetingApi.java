package com.example.doanthuctap.restful;

import com.example.doanthuctap.entity.Meeting;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MeetingApi {
    @GET("/api/meeting/meetings")
    Call<List<Meeting>> getAllMeetings();

    @POST("/api/meeting/meetings")
    Call<Void> createMeeting(@Body Meeting meeting);

}
