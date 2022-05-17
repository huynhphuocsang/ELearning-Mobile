package com.example.elearningptit.remote;

import com.example.elearningptit.config.GlobalVariables;
import com.example.elearningptit.model.CodeVerifySuccessResponse;
import com.example.elearningptit.model.HashCodeVerifyResponse;
import com.example.elearningptit.model.NewPasswordModel;
import com.example.elearningptit.model.RecoveryModelRequest;
import com.example.elearningptit.model.StudentDTO;
import com.example.elearningptit.model.TimelineDTO;
import com.example.elearningptit.model.UpdatePasswordRequestWithVerify;
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

public interface APICallStudent {
    Gson gson = new GsonBuilder().setLenient().create();
    APICallStudent apiCall = new Retrofit.Builder().baseUrl(GlobalVariables.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallStudent.class);


    @GET("student/get-by-student-code")
    Call<List<StudentDTO>> findByStudentCode(@Header("Authorization") String token, @Query("student-code") String studentCode);

    @POST("student/verify-forget-password")
    Call<HashCodeVerifyResponse> verifyForgotPassword(@Query("student-code") String studentCode);

    @POST("student/verify-code")
    Call<CodeVerifySuccessResponse> verifyCode(@Body RecoveryModelRequest recoveryModelRequest);

    @POST("student/recover-password")
    Call<String> updatePassword(@Body UpdatePasswordRequestWithVerify updatePasswordRequestWithVerify);
}
