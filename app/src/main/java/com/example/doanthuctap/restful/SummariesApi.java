package com.example.doanthuctap.restful;

import com.example.doanthuctap.entity.Summary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SummariesApi {
    @GET("/api/summaries/summaries-by-meeting-id/{meeting_id}")
    Call<List<Summary>> getSummariesByMeetingId(@Path("meeting_id") String meetingId);

    @POST("/api/summaries/summaries")
    Call<Void> addSummary(@Body Summary summary);

}
