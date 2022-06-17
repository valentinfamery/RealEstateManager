package com.openclassrooms.realestatemanager.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/";
    private static final Retrofit retrofit = createRetrofit();

    public static ApiInterface getInterface() {
        return retrofit.create(ApiInterface.class);
    }

    public static Gson createGsonBuilder(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    public static Retrofit createRetrofit(){
        //HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        //logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …

// add logging as last interceptor
        //httpClient.addInterceptor(logging);
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(GsonConverterFactory.create(createGsonBuilder()))
                .client(httpClient.build())
                .build();
    }
}
