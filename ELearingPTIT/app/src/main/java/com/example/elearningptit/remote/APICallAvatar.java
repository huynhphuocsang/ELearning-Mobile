package com.example.elearningptit.remote;

import com.example.elearningptit.config.GlobalVariables;
import com.example.elearningptit.model.AvatarResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APICallAvatar {
    Gson gson = new GsonBuilder().setLenient().create();
    APICallAvatar apiCall = new Retrofit.Builder().baseUrl(GlobalVariables.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallAvatar.class);

    @Multipart
    @POST("avatar/upload")
    Call<AvatarResponse> uploadAvatar(@Header("Accept") String json,@Header("Authorization") String token, @Part MultipartBody.Part file);

}
