package com.openclassrooms.realestatemanager.service;



import static com.openclassrooms.realestatemanager.BuildConfig.MAPS_API_KEY;

import com.openclassrooms.realestatemanager.models.resultGeocoding.ResultGeocoding;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("geocode/json?&key=" + MAPS_API_KEY)
    Call<ResultGeocoding> getResultGeocodingResponse(@Query("address") String address);
}
