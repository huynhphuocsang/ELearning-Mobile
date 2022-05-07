package com.example.elearningptit.remote;

import com.example.elearningptit.model.CreditClass;
import com.example.elearningptit.model.CreditClassDetail;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APICallCreditClass {
    String BASE_URL = "http://192.168.1.13:8080/api/";
//    String BASE_URL = "http://192.168.1.6:8080/api/"; // Vu

    Gson gson = new GsonBuilder().create();
    APICallCreditClass apiCall = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallCreditClass.class);

    @GET("credit-class/{pageNo}")
    Call<List<CreditClass>> getCreditClass(@Header("Authorization") String token, @Path("pageNo") int pageNo);

    @GET("credit-class/creditclass-detail?")
    Call<CreditClassDetail> getCreditClassDetail(@Header("Authorization") String token, @Query("creditclass_id") int creditclass_id);



}
