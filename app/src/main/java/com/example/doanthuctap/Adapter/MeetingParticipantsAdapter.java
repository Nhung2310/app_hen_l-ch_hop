package com.example.doanthuctap.Adapter;

import android.content.Context;
import android.util.Log;
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
    public int getCount() {
        return participants.size();
    }

    @Override
    public Meetingparticipants getItem(int position) {
        return participants.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.meeting_participant_item, parent, false);
        }

        // Initialize TextViews
        TextView nameTextView = convertView.findViewById(R.id.participantNameTextView);
        TextView emailTextView = convertView.findViewById(R.id.participantEmailTextView);

        // Get the current participant
        Meetingparticipants participant = getItem(position);

        // Check if participant is not null before setting text
        if (participant != null) {
            nameTextView.setText(participant.getParticipantName());
            emailTextView.setText(participant.getEmail());
        } else {
            // Handle case where participant is null
            Log.e("MeetingParticipantsAdapter", "Participant is null at position: " + position);
        }

        return convertView;
    }


}
