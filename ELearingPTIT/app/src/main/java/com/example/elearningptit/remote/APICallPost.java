package com.example.elearningptit.remote;

import com.example.elearningptit.config.GlobalVariables;
import com.example.elearningptit.model.CreditClassDetailDTO;
import com.example.elearningptit.model.PostCommentDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface APICallPost {
    Gson gson = new GsonBuilder().setLenient().create();
    APICallPost apiCall = new Retrofit.Builder().baseUrl(GlobalVariables.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallPost.class);

    @GET("post/all-comment")
    Call<List<PostCommentDTO>> getAllComments(@Header("Authorization") String token, @Query("post-id") long postId);
}
