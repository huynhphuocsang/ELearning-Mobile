package com.example.elearningptit.remote;

import com.example.elearningptit.config.GlobalVariables;
import com.example.elearningptit.model.NewPasswordModel;
import com.example.elearningptit.model.NotificationPageForUser;
import com.example.elearningptit.model.PostResponseDTO;
import com.example.elearningptit.model.Student;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APICallNotification {

    Gson gson = new GsonBuilder().setLenient().create();
    APICallNotification apiCall = new Retrofit.Builder().baseUrl(GlobalVariables.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallNotification.class);

    @GET("notification/all-notification/{pageNo}")
    Call<NotificationPageForUser> getNotification(@Header("Authorization") String token, @Path("pageNo") int pageNo);


    @PUT("notification/seen")
    Call<String> setSeen(@Header("Authorization") String token, @Query("notification-id") long notificationId);

}
