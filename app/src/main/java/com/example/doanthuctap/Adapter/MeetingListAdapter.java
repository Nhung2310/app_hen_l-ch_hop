package com.example.doanthuctap.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanthuctap.R;
import com.example.doanthuctap.Response.MeetingResponse;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

public class MeetingListAdapter extends RecyclerView.Adapter<MeetingListAdapter.MeetingViewHolder> {
    private final Context context;
    private final List<MeetingResponse> meetingList;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Khai báo định dạng ngày

    public MeetingListAdapter(Context context, List<MeetingResponse> meetingList) {
        this.context = context;
        this.meetingList = meetingList;
    }

    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_meeting, parent, false);
        return new MeetingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingViewHolder holder, int position) {
        MeetingResponse meeting = meetingList.get(position);

        holder.titleTextView.setText("Tên cuộc họp: " + meeting.getTitle());

        // Định dạng thời gian
        String startTime = "N/A";
        String endTime = "N/A";

        if (meeting.getStartTime() != null) {
            startTime = meeting.getStartTime();
        }

        if (meeting.getEndTime() != null) {
            endTime = meeting.getEndTime();
        }

        holder.timeTextView.setText("Thời gian: " + startTime + " - " + endTime);
        String day = "N/A";
        if (meeting.getMeetingDate() != null) {
            day = meeting.getMeetingDate();
        }

        holder.dayTextView.setText("Ngày: " + day);

        // Định dạng và so sánh ngày
        LocalDate today = LocalDate.now();
        LocalDate meetingDate = LocalDate.parse(meeting.getMeetingDate(), dateFormatter);

        if (meetingDate.isBefore(today)) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.past_meeting_color)); // Xám tối cho cuộc họp đã qua
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.future_meeting_color)); // Trắng cho cuộc họp tương lai
        }
    }

    @Override
    public int getItemCount() {
        return meetingList.size();
    }

    public static class MeetingViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView timeTextView;
        TextView dayTextView;

        public MeetingViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
        }
    }
}
