package com.example.elearningptit.remote;

import com.example.elearningptit.config.GlobalVariables;
import com.example.elearningptit.model.Document;
import com.example.elearningptit.model.DocumentResponseData;
import com.example.elearningptit.remote.admin.APICallFolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APICallManagerDocument {
    Gson gson = new GsonBuilder().setLenient().create();
    APICallManagerDocument apiCall = new Retrofit.Builder().baseUrl(GlobalVariables.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallManagerDocument.class);

    @GET ("admin/document/{document-id}")
    Call<Document> dowloadFile (@Header("Authorization") String token, @Path("document-id") Long documentId);

    @Multipart
    @POST("admin/document/upload")
    Call<DocumentResponseData> uploadFile (@Header("Authorization") String token, @Part MultipartBody.Part file, @Query("folder-id") Long folderId);

    @DELETE("admin/document/delete-document")
    Call<String> deleteFile (@Header("Authorization") String token, @Query("document-id") Long documentId);


}
