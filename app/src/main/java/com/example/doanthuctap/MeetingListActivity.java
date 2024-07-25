package com.example.doanthuctap;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.doanthuctap.Adapter.MeetingAdapter;
import com.example.doanthuctap.Retrofit.Constant;
import com.example.doanthuctap.entity.Meeting;
import com.example.doanthuctap.restful.MeetingApi;
import com.example.doanthuctap.util.GsonProvider;

import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MeetingListActivity extends AppCompatActivity {
    private ListView meetingListView;
    private MeetingAdapter meetingAdapter;
    private MeetingApi meetingApi;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);
        meetingListView = findViewById(R.id.meeting_list);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL + "/api/meeting/meetings/")
                .addConverterFactory(GsonConverterFactory.create(GsonProvider.getGson()))
                .build();

        meetingApi = retrofit.create(MeetingApi.class);

        // Fetch data from API
        fetchMeetings();
    }

    private void fetchMeetings() {

                meetingApi.getAllMeetings().enqueue(new Callback<List<Meeting>>() {
                    @Override
                    public void onResponse(Call<List<Meeting>> call, Response<List<Meeting>> response) {
                        if (!response.isSuccessful()) {
                            Log.e("API Error", "Code: " + response.code() + " Message: " + response.message());
                            Toast.makeText(getApplicationContext(), "Failed to fetch meetings!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        List<Meeting> meetings = response.body();
                        if (meetings != null) {
                            for (Meeting meeting : meetings) {
                                Log.d("Meeting", "Meeting ID: " + meeting.getMeetingId());
                                Log.d("Meeting", "Title: " + meeting.getTitle());
                                Log.d("Meeting", "Date: " + meeting.getMeetingDate());
                                Log.d("Meeting", "Start Time: " + meeting.getStartTime());
                                Log.d("Meeting", "End Time: " + meeting.getEndTime());
                                Log.d("Meeting", "Next Meeting Time: " + meeting.getNextMeetingTime());
                            }

                            // Sắp xếp và cập nhật adapter
                            Collections.sort(meetings, (m1, m2) -> {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                                LocalTime startTime1 = LocalTime.parse(m1.getStartTime(), formatter);
                                LocalTime startTime2 = LocalTime.parse(m2.getStartTime(), formatter);
                                return startTime1.compareTo(startTime2);
                            });



                            meetingAdapter = new MeetingAdapter(MeetingListActivity.this, meetings);
                            meetingListView.setAdapter(meetingAdapter);

                            meetingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Meeting selectedMeeting = meetings.get(position);
                                    Intent intent = new Intent(MeetingListActivity.this, MeetingDetailsActivity.class);

                                    intent.putExtra("meeting_id", String.valueOf(selectedMeeting.getMeetingId()));

                                   // intent.putExtra("meeting_id", selectedMeeting.getMeetingId());
                                    intent.putExtra("meeting_title", selectedMeeting.getTitle());
                                    intent.putExtra("meeting_date", selectedMeeting.getMeetingDate().toString());
                                    intent.putExtra("start_time", selectedMeeting.getStartTime().toString());
                                    intent.putExtra("end_time", selectedMeeting.getEndTime().toString());
                                    intent.putExtra("location", selectedMeeting.getLocation());
                                    intent.putExtra("agenda", selectedMeeting.getAgenda());
                                    intent.putExtra("documents", selectedMeeting.getDocuments());
                                    intent.putExtra("result", selectedMeeting.getResult());
                                    intent.putExtra("next_meeting_time", selectedMeeting.getNextMeetingTime().toString());
                                    startActivity(intent);
                                }
                            });
                        }
                    }

            @Override
            public void onFailure(Call<List<Meeting>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to fetch meetings!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
