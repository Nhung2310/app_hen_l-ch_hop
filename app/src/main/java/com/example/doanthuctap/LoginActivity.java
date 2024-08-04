package com.example.doanthuctap;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.doanthuctap.Retrofit.Constant;
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
    private EditText editEmail;
    private EditText editPassword;
    private ImageView passwordToggle;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL + "/api/user/login/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userApi = retrofit.create(UserApi.class);

        // Find views by ID
        editEmail = findViewById(R.id.login_email);
        editPassword = findViewById(R.id.login_password);
        passwordToggle = findViewById(R.id.password_toggle);

        // Set onClickListener for the password toggle
        passwordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordToggle.setImageResource(R.drawable.baseline_disabled_visible_24);
                } else {
                    editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordToggle.setImageResource(R.drawable.baseline_visibility_off_24);
                }
                isPasswordVisible = !isPasswordVisible;
                editPassword.setSelection(editPassword.getText().length());
            }
        });

        // Set onClickListener for the login button
        Button btnSubmit = findViewById(R.id.login_button);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a login request
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.email = editEmail.getText().toString();
                loginRequest.password = editPassword.getText().toString();

                // Call the login API
                userApi.login(loginRequest).enqueue(new Callback<User>() {

                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login thất bại!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        User user = response.body();
                        if (user != null) {
                            Log.d("LoginActivity", "User: " + user.toString()); // Thêm log để xem user object
                            SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("user_id", user.getUserId()); // Lưu user_id
                            editor.apply();
                            String role = user.getRole();
                            if (role != null) {
                                Log.d("LoginActivity", "Role: " + role);
                                // Redirect based on user role
                                if (role.equals("manager")) {
                                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                    finish();
                                } else if (role.equals("member")) {
                                    startActivity(new Intent(LoginActivity.this, MemberActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Vai trò không xác định!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d("LoginActivity", "Role is null");
                                Toast.makeText(getApplicationContext(), "Không thể lấy vai trò từ server!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("LoginActivity", "User is null");
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
