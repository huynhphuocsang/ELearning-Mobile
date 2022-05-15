package com.example.elearningptit.remote;

import com.example.elearningptit.config.GlobalVariables;
import com.example.elearningptit.model.Document;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface APICallManagerDocument {
    Gson gson = new GsonBuilder().create();
    APICallManagerDocument apiCall = new Retrofit.Builder().baseUrl(GlobalVariables.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallManagerDocument.class);

    @GET ("admin/document/{document-id}")
    Call<Document> dowloadFile (@Header("Authorization") String token, @Path("document-id") Long documentId);
}
