package com.example.doanthuctap.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.doanthuctap.R;
import com.example.doanthuctap.entity.Meetingparticipants;

import java.util.List;

public class MeetingParticipantsAdapter extends ArrayAdapter<Meetingparticipants> {

    private Context context;
    private List<Meetingparticipants> participants;

    public MeetingParticipantsAdapter(Context context, List<Meetingparticipants> participants) {
        super(context, R.layout.meeting_participant_item, participants);
        this.context = context;
        this.participants = participants;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.meeting_participant_item, parent, false);
        }

        Meetingparticipants participant = participants.get(position);

        TextView nameTextView = convertView.findViewById(R.id.participantNameTextView);
        TextView emailTextView = convertView.findViewById(R.id.participantEmailTextView);
        TextView roleTextView = convertView.findViewById(R.id.participantRoleTextView);
        TextView statusTextView = convertView.findViewById(R.id.participantStatusTextView);

        nameTextView.setText(participant.getParticipantName());
        emailTextView.setText(participant.getEmail());
        roleTextView.setText(participant.getRole());
        statusTextView.setText(participant.getAttendanceStatus());

        return convertView;
    }
}
