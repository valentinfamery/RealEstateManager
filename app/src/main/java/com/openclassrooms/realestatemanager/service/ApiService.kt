package com.openclassrooms.realestatemanager.service

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import com.openclassrooms.realestatemanager.service.ApiService
import com.openclassrooms.realestatemanager.service.ApiInterface
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

object ApiService {
    const val BASE_URL = "https://maps.googleapis.com/maps/api/"
    private val retrofit = createRetrofit()
    val `interface`: ApiInterface
        get() = retrofit.create(ApiInterface::class.java)

    fun createGsonBuilder(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    fun createRetrofit(): Retrofit {
        //HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        //logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        val httpClient = OkHttpClient.Builder()
        // add your other interceptors …

// add logging as last interceptor
        //httpClient.addInterceptor(logging);
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .callbackExecutor(Executors.newSingleThreadExecutor())
            .addConverterFactory(GsonConverterFactory.create(createGsonBuilder()))
            .client(httpClient.build())
            .build()
    }
}