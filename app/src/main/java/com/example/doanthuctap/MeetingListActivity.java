package com.example.doanthuctap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeetingListActivity extends AppCompatActivity {
    private ListView meetingListView;
    private MeetingAdapter meetingAdapter;
    private List<Meeting> meetingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);
        meetingListView = findViewById(R.id.meeting_list);

        // Tạo dữ liệu giả
        meetingList = new ArrayList<>();
        meetingList.add(new Meeting("Cuộc họp chiến lược", "08:00 - 10:00, 15/07/2024"));
        meetingList.add(new Meeting("Cuộc họp phát triển sản phẩm", "14:00 - 16:00, 16/07/2024"));
        meetingList.add(new Meeting("Cuộc họp kế hoạch tài chính", "09:00 - 11:00, 17/07/2024"));

        // Sắp xếp danh sách theo thời gian cuộc họp
        Collections.sort(meetingList, (m1, m2) -> m2.getMeetingTime().compareTo(m1.getMeetingTime()));

        // Tạo adapter và gán cho ListView
        meetingAdapter = new MeetingAdapter(this, meetingList);
        meetingListView.setAdapter(meetingAdapter);
    }
}