package com.example.elearningptit.remote.admin;

import com.example.elearningptit.config.GlobalVariables;
import com.example.elearningptit.model.FolderDTOResponse;
import com.example.elearningptit.model.FolderRequest;
import com.example.elearningptit.model.PostCommentDTO;
import com.example.elearningptit.model.PostCommentRequest;
import com.example.elearningptit.model.PostRequestDTO;
import com.example.elearningptit.model.PostResponseDTO;
import com.example.elearningptit.remote.APICallPost;
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

public interface APICallFolder {
    Gson gson = new GsonBuilder().setLenient().create();
    APICallFolder apiCall = new Retrofit.Builder().baseUrl(GlobalVariables.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallFolder.class);

    @POST("folder/create-new-folder")
    Call<FolderDTOResponse> createNewFolder(@Header("Authorization") String token, @Body FolderRequest folderRequest);

    @DELETE("folder/delete-folder")
    Call<FolderDTOResponse> deleteFolder(@Header("Authorization") String token, @Query("folder-id") long folderId);

}
