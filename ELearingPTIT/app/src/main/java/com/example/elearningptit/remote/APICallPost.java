package com.example.elearningptit.remote;

import com.example.elearningptit.config.GlobalVariables;
import com.example.elearningptit.model.CreditClassDetailDTO;
import com.example.elearningptit.model.PostCommentDTO;
import com.example.elearningptit.model.PostCommentRequest;
import com.example.elearningptit.model.PostRequestDTO;
import com.example.elearningptit.model.PostResponseDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface APICallPost {
    Gson gson = new GsonBuilder().setLenient().create();
    APICallPost apiCall = new Retrofit.Builder().baseUrl(GlobalVariables.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallPost.class);

    @GET("post/all-comment")
    Call<List<PostCommentDTO>> getAllComments(@Header("Authorization") String token, @Query("post-id") long postId);

    @POST("post/create-new-post")
    Call<PostResponseDTO> createNewPost(@Header("Authorization") String token, @Query("credit-class-id") long creditClassId
                                        , @Body PostRequestDTO postDTO);

    @PUT("post/delete-post")
    Call<PostResponseDTO> deletePost(@Header("Authorization") String token, @Query("post-id") long postId);

    @DELETE("post/delete-comment")
    Call<String> deleteComment(@Header("Authorization") String token, @Query("post-comment-id") long commentId);

    @POST("post/comment")
    Call<PostCommentDTO> comment(@Header("Authorization") String token, @Body PostCommentRequest comment);
}
