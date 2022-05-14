package com.example.elearningptit.remote;

import com.example.elearningptit.config.GlobalVariables;
import com.example.elearningptit.model.AvatarResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APICallAvatar {
    Gson gson = new GsonBuilder().setLenient().create();
    APICallAvatar apiCall = new Retrofit.Builder().baseUrl(GlobalVariables.BASE_URL).client(OkHttp.ok())
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APICallAvatar.class);


    @Multipart
    @POST("avatar/upload")
    Call<AvatarResponse> uploadAvatar(@Header("Authorization") String token, @Part MultipartBody.Part file);

}

class OkHttp {
    static OkHttpClient ok() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor) // This is used to add ApplicationInterceptor.
//                .addNetworkInterceptor(new CustomInterceptor()) //This is used to add NetworkInterceptor.
                .build();
        return okHttpClient;
    }
}

class CustomInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
    /*
    chain.request() returns original request that you can work with(modify, rewrite)
    */
        Request request = chain.request();

        // Here you can rewrite the request

        /*
    chain.proceed(request) is the call which will initiate the HTTP work. This call invokes the request and returns the response as per the request.
        */
        Response response = chain.proceed(request);

        //Here you can rewrite/modify the response

        return response;
    }
}