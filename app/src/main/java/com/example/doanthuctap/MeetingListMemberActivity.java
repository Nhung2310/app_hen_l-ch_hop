package com.example.doanthuctap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.doanthuctap.Adapter.MeetingListAdapter;
import com.example.doanthuctap.Response.MeetingResponse;
import com.example.doanthuctap.Retrofit.Constant;
import com.example.doanthuctap.restful.MeetingparticipantsApi;
import com.example.doanthuctap.util.GsonProvider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MeetingListMemberActivity extends AppCompatActivity {

    private RecyclerView meetingRecyclerView;
    private MeetingListAdapter adapter;
    private List<MeetingResponse> meetingList = new ArrayList<>();
    private MeetingparticipantsApi meetingparticipantsApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list_member);

        meetingRecyclerView = findViewById(R.id.meeting_list);
        meetingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MeetingListAdapter(this, meetingList);
        meetingRecyclerView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL + "/api/meetingparticipants/meeting-ids/")
                .addConverterFactory(GsonConverterFactory.create(GsonProvider.getGson()))
                .build();

        meetingparticipantsApi = retrofit.create(MeetingparticipantsApi.class);

        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);

        if (userId != -1) {
            fetchMeetingsForUser(userId);
        }
    }

    private void fetchMeetingsForUser(int userId) {
        Call<List<MeetingResponse>> call = meetingparticipantsApi.getMeetingIdsForUser(userId);
        call.enqueue(new Callback<List<MeetingResponse>>() {

            @Override
            public void onResponse(Call<List<MeetingResponse>> call, Response<List<MeetingResponse>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("API Response", response.body().toString()); // Log dữ liệu trả về
                        meetingList.clear();
                        meetingList.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MeetingListMemberActivity.this, "No data received", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API Error", "Response not successful. Code: " + response.code() + ", Message: " + response.message());
                    Toast.makeText(MeetingListMemberActivity.this, "Response not successful: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MeetingResponse>> call, Throwable t) {
                Toast.makeText(MeetingListMemberActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
