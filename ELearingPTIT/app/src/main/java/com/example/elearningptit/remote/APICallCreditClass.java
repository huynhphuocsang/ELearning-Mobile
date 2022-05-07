package com.example.elearningptit.remote;

import com.example.elearningptit.model.CreditClass;
import com.example.elearningptit.model.CreditClassPageForUser;
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
import retrofit2.http.Query;

public interface APICallCreditClass {
    String BASE_URL = "http://192.168.1.11:8080/api/";

    Gson gson = new GsonBuilder().create();
    APICallCreditClass apiCall = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallCreditClass.class);

    @GET("credit-class/all/{pageNo}")
    Call<CreditClassPageForUser> getCreditClass(@Header("Authorization") String token, @Path("pageNo") int pageNo);

    @GET("credit-class/all/get-credit-class/{page}")
    Call<CreditClassPageForUser> getCreditClassBySChoolyearDepartSem(@Header("Authorization") String token,@Path("page") int pageNo,
            @Query("schoolyear") String shoolYear, @Query("department_id") int departmentId,@Query("semester") int semester);

    @GET("credit-class/get-credit-class/name-only/{page}")
    Call<CreditClassPageForUser> getCreditClassByName(@Header("Authorization") String token,@Path("page") int pageNo,
            @Query("name") String name);

    @GET("credit-class/get-credit-class/with-name/{page}")
    Call<CreditClassPageForUser> getCreditClassBySChoolyearDepartSemName(@Header("Authorization") String token,@Path("page") int pageNo,
            @Query("schoolyear") String shoolYear, @Query("department_id") int departmentId,@Query("semester") int semester, @Query("name") String name);


}
