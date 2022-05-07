package com.example.elearningptit.remote;

import com.example.elearningptit.model.JwtResponse;
import com.example.elearningptit.model.LoginRequest;
import com.example.elearningptit.model.UserInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APICallSignin {
     String BASE_URL = "http://192.168.1.13:8080/api/";
//    String BASE_URL = "http://192.168.1.6:8080/api/"; // Vu

    Gson gson = new GsonBuilder().create();
    APICallSignin apiCall = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallSignin.class);

    @POST("auth/signin")
    Call<JwtResponse> userLogin(@Body LoginRequest loginRequest);

}
