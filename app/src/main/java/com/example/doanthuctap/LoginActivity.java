package com.example.doanthuctap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doanthuctap.entity.User;
import com.example.doanthuctap.request.LoginRequest;
import com.example.doanthuctap.restful.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private UserApi userApi;

    EditText editEmail;
    EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL + "/api/user/login/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userApi = retrofit.create(UserApi.class);

        editEmail = findViewById(R.id.login_email);
        editPassword = findViewById(R.id.login_password);

        Button btnSubmit = findViewById(R.id.login_button);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.email = editEmail.getText().toString();
                loginRequest.password = editPassword.getText().toString();

                userApi.login(loginRequest).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login thất bại!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        User user = response.body();
                        if (user != null) {
                            String role = user.getRole();
                            if (role != null) {
                                if (role.equals("manage")) {
                                    // Chuyển hướng sang AdminActivity
                                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                    startActivity(intent);
                                    finish(); // Kết thúc LoginActivity sau khi chuyển hướng
                                } else if (role.equals("member")) {
                                    // Chuyển hướng sang MemberActivity
                                    Intent intent = new Intent(LoginActivity.this, MemberActivity.class);
                                    startActivity(intent);
                                    finish(); // Kết thúc LoginActivity sau khi chuyển hướng
                                } else {
                                    Toast.makeText(getApplicationContext(), "Vai trò không xác định!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Không thể lấy vai trò từ server!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Không thể lấy thông tin người dùng từ server!", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Login thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}