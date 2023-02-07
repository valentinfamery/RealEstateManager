package com.openclassrooms.realestatemanager.service

import com.openclassrooms.realestatemanager.BuildConfig
import retrofit2.http.GET
import com.openclassrooms.realestatemanager.domain.models.resultGeocoding.ResultGeocoding
import retrofit2.Response
import retrofit2.http.Query

interface ApiInterface {
    @GET("geocode/json?&key=" + BuildConfig.MAPS_API_KEY)
    suspend fun getResultGeocodingResponse(@Query("address") address: String?): Response<ResultGeocoding>
}