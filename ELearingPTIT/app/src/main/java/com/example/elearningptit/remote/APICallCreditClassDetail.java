package com.example.elearningptit.remote;

import com.example.elearningptit.config.GlobalVariables;
import com.example.elearningptit.model.CreditClassDetailDTO;
import com.example.elearningptit.model.PostDTOWithComment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import kotlin.ParameterName;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APICallCreditClassDetail {
    Gson gson = new GsonBuilder().setLenient().create();
    APICallCreditClassDetail apiCall = new Retrofit.Builder().baseUrl(GlobalVariables.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallCreditClassDetail.class);

    @GET("credit-class/creditclass-detail")
    Call<CreditClassDetailDTO> getCreditClassDelta(@Header("Authorization") String token, @Query("creditclass_id") long creditClassId);

    @GET("credit-class/creditclass-list-post")
    Call<List<PostDTOWithComment>> getPostsInClass(@Header("Authorization") String token, @Query("creditclass_id") long creditClassId);

}
