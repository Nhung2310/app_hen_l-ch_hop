package com.example.doanthuctap;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.doanthuctap.Adapter.MeetingListAdapter;
import com.example.doanthuctap.Response.MeetingIdResponse;
import com.example.doanthuctap.Response.MeetingResponse;
import com.example.doanthuctap.Retrofit.Constant;
import com.example.doanthuctap.entity.Meeting;
import com.example.doanthuctap.restful.MeetingApi;
import com.example.doanthuctap.restful.MeetingparticipantsApi;
import com.example.doanthuctap.util.GsonProvider;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MeetingListMemberActivity extends AppCompatActivity {
    private ImageView filterIcon;
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

        filterIcon = findViewById(R.id.filter_icon);

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
        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });
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

    private void sortMeetingResponses(List<MeetingResponse> meetingResponses) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        Collections.sort(meetingResponses, (m1, m2) -> {
            // Compare dates
            int dateComparison = LocalDate.parse(m2.getMeetingDate(), dateFormatter)
                    .compareTo(LocalDate.parse(m1.getMeetingDate(), dateFormatter));

            // If dates are equal, compare times
            if (dateComparison == 0) {
                LocalTime time1 = LocalTime.parse(m1.getStartTime(), timeFormatter);
                LocalTime time2 = LocalTime.parse(m2.getStartTime(), timeFormatter);
                return time2.compareTo(time1); // Latest to oldest
            }

            return dateComparison;
        });
    }


    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn tùy chọn lọc");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter, null);
        builder.setView(dialogView);

        Button filterLatestButton = dialogView.findViewById(R.id.filter_latest);
        Button filterOldestButton = dialogView.findViewById(R.id.filter_oldest);

        AlertDialog dialog = builder.create();

        filterLatestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (meetingList != null && !meetingList.isEmpty()) {
                    sortMeetingResponses(meetingList); // Use the new sorting method
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Danh sách cuộc họp không có dữ liệu!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        filterOldestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (meetingList != null && !meetingList.isEmpty()) {
                    Collections.sort(meetingList, (m1, m2) -> {
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

                        // Compare dates
                        int dateComparison = LocalDate.parse(m1.getMeetingDate(), dateFormatter)
                                .compareTo(LocalDate.parse(m2.getMeetingDate(), dateFormatter));

                        // If dates are equal, compare times
                        if (dateComparison == 0) {
                            LocalTime time1 = LocalTime.parse(m1.getStartTime(), timeFormatter);
                            LocalTime time2 = LocalTime.parse(m2.getStartTime(), timeFormatter);
                            return time1.compareTo(time2); // Oldest to latest
                        }

                        return dateComparison;
                    });
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Danh sách cuộc họp không có dữ liệu!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", null);

        dialog.show();
    }

}
