package com.example.doanthuctap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.doanthuctap.Retrofit.Constant;
import com.example.doanthuctap.restful.MeetingApi;
import com.example.doanthuctap.util.GsonProvider;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MemberActivity extends AppCompatActivity {
    private MeetingApi meetingApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL + "/api/meeting/meetings/")
                .addConverterFactory(GsonConverterFactory.create(GsonProvider.getGson()))
                .build();

        meetingApi = retrofit.create(MeetingApi.class);

        // Setup the logout button
        ImageButton logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle logout logic here
                Toast.makeText(MemberActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
                finish(); // Close the MemberActivity
            }
        });

        // Retrieve and display the list of meetings
       // getMeetings();
    }
}