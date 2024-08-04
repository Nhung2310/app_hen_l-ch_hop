package com.example.doanthuctap.restful;

import com.example.doanthuctap.Response.MeetingIdResponse;
import com.example.doanthuctap.Response.MeetingResponse;
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

    ///@GET("meeting-ids/{userId}")
   // Call<List<MeetingResponse>> getMeetingIdsForUser(@Path("userId") int userId);
    @GET("/api/meetingparticipants/meeting-ids/{userId}")
    Call<List<MeetingIdResponse>> getMeetingIdsForUser(@Path("userId") int userId);
   // @GET("/api/meetingparticipants/meeting-ids/{userId}")
   // Call<List<Integer>> getMeetingIdsForUser(@Path("userId") int userId);

}
