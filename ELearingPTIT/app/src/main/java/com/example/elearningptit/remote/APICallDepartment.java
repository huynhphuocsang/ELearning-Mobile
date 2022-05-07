package com.example.elearningptit.remote;

import com.example.elearningptit.model.Department;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface APICallDepartment {
    String BASE_URL = "http://192.168.1.13:8080/api/";
//    String BASE_URL = "http://192.168.1.6:8080/api/"; // Vu

    Gson gson = new GsonBuilder().create();
    APICallDepartment apiCall = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallDepartment.class);

    @GET("department/all")
    Call<List<Department>> getAllDepartment(@Header("Authorization") String token);
}
