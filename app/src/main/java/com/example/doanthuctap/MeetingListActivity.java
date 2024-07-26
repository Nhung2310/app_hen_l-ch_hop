package com.example.doanthuctap;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.doanthuctap.Adapter.MeetingAdapter;
import com.example.doanthuctap.Retrofit.Constant;
import com.example.doanthuctap.entity.Meeting;
import com.example.doanthuctap.restful.MeetingApi;
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

public class MeetingListActivity extends AppCompatActivity {
    private ListView meetingListView;
    private MeetingAdapter meetingAdapter;
    private MeetingApi meetingApi;
    private ImageView filterIcon;
    private List<Meeting> meetings = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);

        meetingListView = findViewById(R.id.meeting_list);
        filterIcon = findViewById(R.id.filter_icon);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL + "/api/meeting/meetings/")
                .addConverterFactory(GsonConverterFactory.create(GsonProvider.getGson()))
                .build();

        meetingApi = retrofit.create(MeetingApi.class);

        // Fetch data from API
        fetchMeetings();

        // Handle filter icon click
        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });
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

                List<Meeting> fetchedMeetings = response.body();
                if (fetchedMeetings != null) {
                    meetings.clear();
                    meetings.addAll(fetchedMeetings);

                    // Sắp xếp và cập nhật adapter
                    sortMeetings(meetings);

                    if (meetingAdapter == null) {
                        meetingAdapter = new MeetingAdapter(MeetingListActivity.this, meetings);
                        meetingListView.setAdapter(meetingAdapter);
                    } else {
                        meetingAdapter.notifyDataSetChanged();
                    }

                    meetingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Meeting selectedMeeting = meetings.get(position);
                            Intent intent = new Intent(MeetingListActivity.this, MeetingDetailsActivity.class);
                            intent.putExtra("meeting_id", String.valueOf(selectedMeeting.getMeetingId()));
                            intent.putExtra("meeting_title", selectedMeeting.getTitle());
                            intent.putExtra("meeting_date", selectedMeeting.getMeetingDate());
                            intent.putExtra("start_time", selectedMeeting.getStartTime());
                            intent.putExtra("end_time", selectedMeeting.getEndTime());
                            intent.putExtra("location", selectedMeeting.getLocation());
                            intent.putExtra("agenda", selectedMeeting.getAgenda());
                            intent.putExtra("documents", selectedMeeting.getDocuments());
                            intent.putExtra("result", selectedMeeting.getResult());
                            intent.putExtra("next_meeting_time", selectedMeeting.getNextMeetingTime());
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

    private void sortMeetings(List<Meeting> meetings) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        Collections.sort(meetings, (m1, m2) -> {
            // So sánh ngày
            int dateComparison = LocalDate.parse(m2.getMeetingDate(), dateFormatter)
                    .compareTo(LocalDate.parse(m1.getMeetingDate(), dateFormatter));

            // Nếu ngày bằng nhau, so sánh giờ
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
                if (meetings != null && !meetings.isEmpty()) {
                    sortMeetings(meetings);
                    meetingAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Danh sách cuộc họp không có dữ liệu!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        filterOldestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (meetings != null && !meetings.isEmpty()) {
                    Collections.sort(meetings, (m1, m2) -> {
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

                        // So sánh ngày
                        int dateComparison = LocalDate.parse(m1.getMeetingDate(), dateFormatter)
                                .compareTo(LocalDate.parse(m2.getMeetingDate(), dateFormatter));

                        // Nếu ngày bằng nhau, so sánh giờ
                        if (dateComparison == 0) {
                            LocalTime time1 = LocalTime.parse(m1.getStartTime(), timeFormatter);
                            LocalTime time2 = LocalTime.parse(m2.getStartTime(), timeFormatter);
                            return time1.compareTo(time2); // Oldest to latest
                        }

                        return dateComparison;
                    });
                    meetingAdapter.notifyDataSetChanged();
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
