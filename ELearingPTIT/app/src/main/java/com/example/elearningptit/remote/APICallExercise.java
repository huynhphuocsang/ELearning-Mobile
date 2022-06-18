package com.example.elearningptit.remote;

import com.example.elearningptit.config.GlobalVariables;
import com.example.elearningptit.model.AvatarResponse;
import com.example.elearningptit.model.CreateExerciseResponse;
import com.example.elearningptit.model.CreditClassDetailDTO;
import com.example.elearningptit.model.Department;
import com.example.elearningptit.model.Document;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APICallExercise {
    Gson gson = new GsonBuilder().setLenient().create();
    APICallExercise apiCall = new Retrofit.Builder().baseUrl(GlobalVariables.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallExercise.class);


    @GET("excercise/document-info")
    Call<List<Document>> getExerciseDocument(@Header("Authorization") String token, @Query("excercise-id") long exerciseID);

    @Multipart
    @POST("excercise/create-new")
    Call<CreateExerciseResponse> createNewExercise(@Header("Authorization") String token, @Part MultipartBody.Part startTime, @Part MultipartBody.Part endTime, @Part MultipartBody.Part title, @Part MultipartBody.Part creditClassId, @Part MultipartBody.Part excerciseContent, @Part MultipartBody.Part files);
//
//    @POST("avatar/upload")
//    Call<AvatarResponse> uploadAvatar(@Header("Authorization") String token, @Part MultipartBody.Part file);
}
