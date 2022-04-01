package com.example.elearningptit.remote;

import com.example.elearningptit.model.JwtResponse;
import com.example.elearningptit.model.LoginRequest;
import com.example.elearningptit.model.NewPasswordModel;
import com.example.elearningptit.model.UserInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface APICallUser {
     String BASE_URL = "http://192.168.1.4:8080/api/";

    Gson gson = new GsonBuilder().setLenient().create();
    APICallUser apiCall = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallUser.class);


    @GET("auth/get-account-info")
    Call<String> getAccountInfo(@Header("Authorization") String token);

    @GET("user/get-user-info")
    Call<UserInfo> getUserInfo(@Header("Authorization") String token);

    @PUT("user/update-new-password")
    Call<String> updateNewPassword(@Header("Authorization") String token, @Body NewPasswordModel newPasswordModel);
}
