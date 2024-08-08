package com.example.doanthuctap.restful;

import com.example.doanthuctap.Response.MeetingResponse;
import com.example.doanthuctap.entity.Meeting;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface MeetingApi {
    @GET("/api/meeting/meetings")
    Call<List<Meeting>> getAllMeetings();

    @POST("/api/meeting/meetings")
    Call<Meeting> createMeeting(@Body Meeting meeting);

    @GET("/api/meeting/meetings/{id}")
    Call<MeetingResponse> getMeetingById(@Path("id") int meetingId);

    @Multipart
    @POST("/api/meeting/meetings-with-files")
    Call<Meeting> createMeetingWithFiles(
            @Part("title") RequestBody title,
            @Part("meeting_date") RequestBody meetingDate,
            @Part("location") RequestBody location,
            @Part("agenda") RequestBody agenda,
            @Part("result") RequestBody result,
            @Part("start_time") RequestBody startTime,
            @Part("end_time") RequestBody endTime,
            @Part("next_meeting_time") RequestBody nextMeetingTime,
            @Part List<MultipartBody.Part> files
    );
}
