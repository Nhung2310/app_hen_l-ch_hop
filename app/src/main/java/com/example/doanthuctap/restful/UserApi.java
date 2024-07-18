package com.example.doanthuctap.restful;

import com.example.doanthuctap.entity.User;
import com.example.doanthuctap.request.LoginRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {
    @POST("/api/user/login")
    Call<User> login(@Body LoginRequest loginRequest);
}
