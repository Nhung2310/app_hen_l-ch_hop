package com.example.doanthuctap.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.doanthuctap.R;
import com.example.doanthuctap.entity.Meeting;

import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.LocalTime;

import java.util.List;

public class MeetingAdapter extends ArrayAdapter<Meeting> {
    private final Context context;
    private final List<Meeting> meetings;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public MeetingAdapter(Context context, List<Meeting> meetings) {
        super(context, R.layout.item_meeting, meetings);
        this.context = context;
        this.meetings = meetings;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_meeting, parent, false);
        }

        Meeting meeting = meetings.get(position);

        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView timeTextView = convertView.findViewById(R.id.timeTextView);
        TextView dayTextView=convertView.findViewById(R.id.dayTextView);

        titleTextView.setText("Tên cuộc họp: " + meeting.getTitle());

        // Định dạng thời gian
        String startTime = "N/A";
        String endTime = "N/A";

        if (meeting.getStartTime() != null) {
            startTime = meeting.getStartTime();
        }

        if (meeting.getEndTime() != null) {
            endTime = meeting.getEndTime();
        }

        timeTextView.setText("Thời gian: " + startTime + " - " + endTime);
        String day = "N/A";
        if (meeting.getMeetingDate() != null) {
            day = meeting.getMeetingDate().toString();
        }

        dayTextView.setText("Ngày: " + day);
        return convertView;
    }
}
