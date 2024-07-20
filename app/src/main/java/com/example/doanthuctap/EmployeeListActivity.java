package com.example.doanthuctap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.doanthuctap.Adapter.EmployeeAdapter;
import com.example.doanthuctap.Retrofit.Constant;
import com.example.doanthuctap.entity.User;
import com.example.doanthuctap.restful.UserApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmployeeListActivity extends AppCompatActivity {
    private ListView employeeListView;
    private EmployeeAdapter employeeAdapter;
    private List<User> employeeList;
    private UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);
        employeeListView = findViewById(R.id.employee_list);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL + "/api/user/users/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        userApi = retrofit.create(UserApi.class);

        // Fetch data from API
        fetchEmployees();
    }

    private void fetchEmployees() {
        userApi.getAllUsers().enqueue(new Callback<List<User>>() {

            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    Log.e("API Error", "Code: " + response.code() + " Message: " + response.message());
                    Toast.makeText(getApplicationContext(), "Failed to fetch employees!", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<User> users = response.body();
                if (users != null) {
                    // Filter out managers
                    employeeList = new ArrayList<>();
                    for (User user : users) {
                        Log.d("Employee", "FullName: " + user.getFullName()); // Log giá trị fullName
                        if (!"manager".equals(user.getRole())) {
                            employeeList.add(user);
                        }
                    }

                    // Set up the adapter with the filtered employee list
                    employeeAdapter = new EmployeeAdapter(EmployeeListActivity.this, employeeList);
                    employeeListView.setAdapter(employeeAdapter);
                }
            }


            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to fetch employees!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
