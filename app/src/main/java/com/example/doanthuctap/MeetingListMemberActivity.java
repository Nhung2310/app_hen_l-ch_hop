package com.example.doanthuctap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.doanthuctap.Adapter.MeetingListAdapter;
import com.example.doanthuctap.Response.MeetingIdResponse;
import com.example.doanthuctap.Response.MeetingResponse;
import com.example.doanthuctap.Retrofit.Constant;
import com.example.doanthuctap.restful.MeetingApi;
import com.example.doanthuctap.restful.MeetingparticipantsApi;
import com.example.doanthuctap.util.GsonProvider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MeetingListMemberActivity extends AppCompatActivity {

    private RecyclerView meetingRecyclerView;
    private MeetingListAdapter adapter;
    private List<MeetingResponse> meetingList = new ArrayList<>();
    private MeetingApi meetingApi;
    private MeetingparticipantsApi meetingparticipantsApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list_member);

        meetingRecyclerView = findViewById(R.id.meeting_list);
        meetingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MeetingListAdapter(this, meetingList, this::onMeetingItemClick);
        meetingRecyclerView.setAdapter(adapter);

        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(Constant.URL + "/api/meeting/meetings/")
                .addConverterFactory(GsonConverterFactory.create(GsonProvider.getGson()))
                .build();
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(Constant.URL + "/api/meetingparticipants/meeting-ids/")
                .addConverterFactory(GsonConverterFactory.create(GsonProvider.getGson()))
                .build();

        meetingApi = retrofit1.create(MeetingApi.class);
        meetingparticipantsApi = retrofit2.create(MeetingparticipantsApi.class);

        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);

        if (userId != -1) {
            fetchMeetingIdsForUser(userId);
        }
    }

    private void fetchMeetingIdsForUser(int userId) {
        Call<List<MeetingIdResponse>> call = meetingparticipantsApi.getMeetingIdsForUser(userId);
        call.enqueue(new Callback<List<MeetingIdResponse>>() {
            @Override
            public void onResponse(Call<List<MeetingIdResponse>> call, Response<List<MeetingIdResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MeetingIdResponse> meetingIdResponses = response.body();
                    List<Integer> meetingIds = new ArrayList<>();
                    for (MeetingIdResponse meetingIdResponse : meetingIdResponses) {
                        meetingIds.add(meetingIdResponse.getMeetingId());
                    }
                    fetchMeetingDetails(meetingIds);
                } else {
                    Toast.makeText(MeetingListMemberActivity.this, "No data received", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MeetingIdResponse>> call, Throwable t) {
                Toast.makeText(MeetingListMemberActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMeetingDetails(List<Integer> meetingIds) {
        for (int id : meetingIds) {
            Call<MeetingResponse> call = meetingApi.getMeetingById(id);
            call.enqueue(new Callback<MeetingResponse>() {
                @Override
                public void onResponse(Call<MeetingResponse> call, Response<MeetingResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        MeetingResponse meeting = response.body();
                        meetingList.add(meeting);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MeetingListMemberActivity.this, "No data received for meeting ID: " + id, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MeetingResponse> call, Throwable t) {
                    Toast.makeText(MeetingListMemberActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void onMeetingItemClick(MeetingResponse meeting) {
        Intent intent = new Intent(MeetingListMemberActivity.this, MeetingDetailsActivity.class);
        intent.putExtra("meeting_id", String.valueOf(meeting.getMeetingId()));
        // Log giá trị meeting_id
        Log.d("MeetingListMemberActivity", "Passing meeting_id: " + meeting.getMeetingId());
        intent.putExtra("meeting_title", meeting.getTitle());
        intent.putExtra("meeting_date", meeting.getMeetingDate());
        intent.putExtra("start_time", meeting.getStartTime());
        intent.putExtra("end_time", meeting.getEndTime());
        intent.putExtra("location", meeting.getLocation());
        intent.putExtra("agenda", meeting.getAgenda());
        intent.putExtra("documents", meeting.getDocuments());
        intent.putExtra("result", meeting.getResult());
        intent.putExtra("next_meeting_time", meeting.getNextMeetingTime());
        startActivity(intent);
    }
}
