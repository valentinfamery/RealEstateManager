package com.openclassrooms.realestatemanager.service

import com.openclassrooms.realestatemanager.BuildConfig
import retrofit2.http.GET
import com.openclassrooms.realestatemanager.domain.models.resultGeocoding.ResultGeocoding
import retrofit2.Call
import retrofit2.http.Query

interface ApiInterface {
    @GET("geocode/json?&key=" + BuildConfig.MAPS_API_KEY)
    fun getResultGeocodingResponse(@Query("address") address: String?): Call<ResultGeocoding?>?
}