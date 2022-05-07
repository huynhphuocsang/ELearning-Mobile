package com.example.elearningptit.remote;

import com.example.elearningptit.config.GlobalVariables;
import com.example.elearningptit.model.JwtResponse;
import com.example.elearningptit.model.LoginRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APICallSchoolYear {

    String BASE_URL = "http://192.168.1.13:8080/api/";
//    String BASE_URL = "http://192.168.1.6:8080/api/"; // Vu
    //String BASE_URL = "http://192.168.7.109:8080/api/";


    Gson gson = new GsonBuilder().create();
    APICallSchoolYear apiCall = new Retrofit.Builder().baseUrl(GlobalVariables.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallSchoolYear.class);

    @GET("school-year/get-all")
    Call<List<String>> getAllSchoolYear(@Header("Authorization") String token);
}
