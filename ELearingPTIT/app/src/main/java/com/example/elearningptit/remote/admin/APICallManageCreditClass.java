package com.example.elearningptit.remote.admin;

import com.example.elearningptit.config.GlobalVariables;
import com.example.elearningptit.model.StudentCodeDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface APICallManageCreditClass {
    Gson gson = new GsonBuilder().setLenient().create();
    APICallManageCreditClass apiCall = new Retrofit.Builder().baseUrl(GlobalVariables.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallManageCreditClass.class);

    @POST("admin/creditclass/add-student-to-credit-class")
    Call<String> addStudentToCreditClass(@Header("Authorization") String token, @Query("credit-class-id") long creditClassId, @Body StudentCodeDTO studentCode);

    @PUT("admin/creditclass/remove-student-from-credit-class")
    Call<String> removeStudentToCreditClass(@Header("Authorization") String token, @Query("credit-class-id") long creditClassId, @Body StudentCodeDTO studentCode);

}
