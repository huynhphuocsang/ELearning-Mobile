package com.example.elearningptit.remote;

import com.example.elearningptit.config.GlobalVariables;
import com.example.elearningptit.model.JwtResponse;
import com.example.elearningptit.model.LoginRequest;
import com.example.elearningptit.model.MarkDTO;
import com.example.elearningptit.model.StudentSubmitExercise;
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

public interface APICallSubmit {
    Gson gson = new GsonBuilder().setLenient().create();
    APICallSubmit apiCall = new Retrofit.Builder().baseUrl(GlobalVariables.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallSubmit.class);

    @GET("submit/get-list-submit?")
    Call<List<StudentSubmitExercise>> getListStudentSubmitExercise(@Header("Authorization") String token, @Query("excerciseId") int excerciseId);

    @PUT("submit/mark")
    Call<String> putSubmitMark(@Header("Authorization") String token, @Body MarkDTO markDTO);
}
