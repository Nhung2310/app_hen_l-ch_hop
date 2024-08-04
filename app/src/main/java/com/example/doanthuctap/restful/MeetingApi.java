package com.example.doanthuctap.restful;

import com.example.doanthuctap.Response.MeetingResponse;
import com.example.doanthuctap.entity.Meeting;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MeetingApi {
    @GET("/api/meeting/meetings")
    Call<List<Meeting>> getAllMeetings();

    @POST("/api/meeting/meetings")
//  Call<Void> createMeeting(@Body Meeting meeting);
    Call<Meeting> createMeeting(@Body Meeting meeting);
   // @POST("/api/meeting/meetings")
  //  Call<MeetingResponse> createMeeting(@Body Meeting meeting);
   @GET("/api/meeting/meetings/{id}")
   Call<MeetingResponse> getMeetingById(@Path("id") int meetingId);

}
