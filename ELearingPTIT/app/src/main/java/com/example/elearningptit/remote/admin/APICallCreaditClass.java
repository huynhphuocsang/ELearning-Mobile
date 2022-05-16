package com.example.elearningptit.remote.admin;

import com.example.elearningptit.config.GlobalVariables;
import com.example.elearningptit.model.Student;
import com.example.elearningptit.remote.APICallCreditClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface APICallCreaditClass {
    Gson gson = new GsonBuilder().create();
    APICallCreaditClass apiCall = new Retrofit.Builder().baseUrl(GlobalVariables.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallCreaditClass.class);

    @POST("admin/creditclass/add-student-to-credit-class?")
    Call<String> getStudentAdd (@Header("Authorization") String token, @Body Student student);

    @PUT("admin/creditclass/remove-student-from-credit-class?")
    Call<Student> deleteStudent(@Header("Authorization") String token, @Query("credit-class-id") String studentCode);
}
