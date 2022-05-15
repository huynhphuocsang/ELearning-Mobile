package com.example.elearningptit.remote;

import com.example.elearningptit.config.GlobalVariables;
import com.example.elearningptit.model.CreditClass;
import com.example.elearningptit.model.ExerciseSubmit;
import com.example.elearningptit.model.NewPasswordModel;
import com.example.elearningptit.model.StudentJoinClassRequestDTO;
import com.example.elearningptit.model.TimelineDTO;
import com.example.elearningptit.model.UserInfo;
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
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface APICallTeacher {
    Gson gson = new GsonBuilder().setLenient().create();
    APICallTeacher apiCall = new Retrofit.Builder().baseUrl(GlobalVariables.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallTeacher.class);



    @GET("user/timetable-by-time-teacher")
    Call<List<TimelineDTO>> getTimetableTeacher(@Header("Authorization") String token, @Query("date")String date);


    @GET("teacher/timetable-semester")
    Call<List<CreditClass>> getTeacherCreditClass(@Header("Authorization") String token);

}
