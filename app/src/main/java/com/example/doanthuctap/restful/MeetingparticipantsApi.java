package com.example.doanthuctap.restful;

import com.example.doanthuctap.entity.Meeting;
import com.example.doanthuctap.entity.Meetingparticipants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MeetingparticipantsApi {
    @POST("/api/meetingparticipants/meetingparticipants")
    Call<Void> createParticipant(@Body Meetingparticipants Meetingparticipants );
   // @GET("/api/meetingparticipants/meetingparticipants/{meetingId}")
   // Call<List<Meetingparticipants>> getParticipantsByMeetingId(@Path("meetingId") String meetingId);
    @GET("{meetingId}") // Định nghĩa tham số động
    Call<List<Meetingparticipants>> getParticipantsByMeetingId(@Path("meetingId") String meetingId);

}
