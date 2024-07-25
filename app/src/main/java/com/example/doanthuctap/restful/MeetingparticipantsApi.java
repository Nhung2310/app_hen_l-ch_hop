package com.example.doanthuctap.restful;

import com.example.doanthuctap.entity.Meeting;
import com.example.doanthuctap.entity.Meetingparticipants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MeetingparticipantsApi {
    @POST("/api/meetingparticipants/meetingparticipants")
    Call<Void> createParticipant(@Body Meetingparticipants Meetingparticipants );

}
