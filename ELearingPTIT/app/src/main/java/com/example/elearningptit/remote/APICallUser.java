package com.example.elearningptit.remote;


import com.example.elearningptit.model.ExerciseSubmit;
import com.example.elearningptit.config.GlobalVariables;
import com.example.elearningptit.model.CreditClass;
import com.example.elearningptit.model.JwtResponse;
import com.example.elearningptit.model.LoginRequest;
import com.example.elearningptit.model.NewPasswordModel;
import com.example.elearningptit.model.StudentJoinClassRequestDTO;
import com.example.elearningptit.model.TimelineDTO;
import com.example.elearningptit.model.UserInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.List;

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
import retrofit2.http.Query;

public interface APICallUser {

    Gson gson = new GsonBuilder().setLenient().create();
    APICallUser apiCall = new Retrofit.Builder().baseUrl(GlobalVariables.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallUser.class);


    @GET("auth/get-account-info")
    Call<String> getAccountInfo(@Header("Authorization") String token);

    @GET("user/get-user-info")
    Call<UserInfo> getUserInfo(@Header("Authorization") String token);

    @GET("user/timetable-by-time")
    Call<List<TimelineDTO>> getTimetable(@Header("Authorization") String token, @Query("date")String date);

    @PUT("user/update-new-password")
    Call<String> updateNewPassword(@Header("Authorization") String token, @Body NewPasswordModel newPasswordModel);


    @POST("user/join-class")
    Call<String> joinClass(@Header("Authorization") String token, @Body StudentJoinClassRequestDTO studentJoinClassRequestDTO);

    @POST("user/check-joined")
    Call<String> checkJoined(@Header("Authorization") String token,@Query("creditclass-id") long creditClassId );

    @GET("user/submit-info")
    Call<ExerciseSubmit> getExerciseSubmit(@Header("Authorization") String token, @Query("excercise-id") int excercise_id);

    @GET("user/registration")
    Call<List<CreditClass>> getUserRegistration(@Header("Authorization") String token);



}
