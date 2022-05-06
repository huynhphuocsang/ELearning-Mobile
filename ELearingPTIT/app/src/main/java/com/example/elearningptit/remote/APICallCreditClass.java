package com.example.elearningptit.remote;

import com.example.elearningptit.model.CreditClass;
import com.example.elearningptit.model.Department;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface APICallCreditClass {
    String BASE_URL = "http://192.168.7.109:8080/api/";

    Gson gson = new GsonBuilder().create();
    APICallCreditClass apiCall = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallCreditClass.class);

    @GET("credit-class/{pageNo}")
    Call<List<CreditClass>> getCreditClass(@Header("Authorization") String token, @Path("pageNo") int pageNo);
}
