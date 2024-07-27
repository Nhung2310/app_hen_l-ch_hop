package com.example.doanthuctap.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.doanthuctap.R;
import com.example.doanthuctap.entity.Meetingparticipants;

import java.util.List;

public class MeetingParticipantsAdapter extends ArrayAdapter<Meetingparticipants> {
    private final Context context;
    private final List<Meetingparticipants> participants;

    public MeetingParticipantsAdapter(Context context, List<Meetingparticipants> participants) {
        super(context, R.layout.meeting_participant_item, participants);
        this.context = context;
        this.participants = participants;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.meeting_participant_item, parent, false);

        TextView nameTextView = rowView.findViewById(R.id.participantNameTextView);
        TextView roleTextView = rowView.findViewById(R.id.participantEmailTextView);

        Meetingparticipants participant = participants.get(position);
        nameTextView.setText(participant.getParticipantName());
        roleTextView.setText(participant.getEmail());

        return rowView;
    }
}
