package com.example.elearningptit.remote;

import com.example.elearningptit.config.GlobalVariables;
import com.example.elearningptit.model.CreditClass;
import com.example.elearningptit.model.CreditClassDetail;
import com.example.elearningptit.model.CreditClassPageForUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APICallCreditClass {
    Gson gson = new GsonBuilder().create();
    APICallCreditClass apiCall = new Retrofit.Builder().baseUrl(GlobalVariables.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallCreditClass.class);


    @GET("credit-class/all/{pageNo}")
    Call<CreditClassPageForUser> getCreditClass(@Header("Authorization") String token, @Path("pageNo") int pageNo);

    @GET("credit-class/all/get-credit-class/{page}")
    Call<CreditClassPageForUser> getCreditClassBySChoolyearDepartSem(@Header("Authorization") String token, @Path("page") int pageNo,
                                                                     @Query("schoolyear") String shoolYear, @Query("department_id") int departmentId, @Query("semester") int semester);

    @GET("credit-class/get-credit-class/name-only/{page}")
    Call<CreditClassPageForUser> getCreditClassByName(@Header("Authorization") String token, @Path("page") int pageNo,
                                                      @Query("name") String name);

    @GET("credit-class/get-credit-class/with-name/{page}")
    Call<CreditClassPageForUser> getCreditClassBySChoolyearDepartSemName(@Header("Authorization") String token, @Path("page") int pageNo,
                                                                         @Query("schoolyear") String shoolYear, @Query("department_id") int departmentId, @Query("semester") int semester, @Query("name") String name);

    @GET("credit-class/{pageNo}")
    Call<List<CreditClass>> getCreditClass2(@Header("Authorization") String token, @Path("pageNo") int pageNo);

    @GET("credit-class/creditclass-detail?")
    Call<CreditClassDetail> getCreditClassDetail(@Header("Authorization") String token, @Query("creditclass_id") int creditclass_id);

}
