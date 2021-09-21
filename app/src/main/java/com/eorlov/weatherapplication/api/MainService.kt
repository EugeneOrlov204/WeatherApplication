package com.eorlov.weatherapplication.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface MainService {

    @GET("data/2.5/weather?")
    suspend fun getCurrentWeatherData(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("APPID") app_id: String?
    ): Response<WeatherResponse?>?

    @GET("geo/1.0/direct?")
    suspend fun getLocation(
        @Query("q") cityName: String?,
        @Query("APPID") app_id: String?
    ) : Response<ArrayList<GeocodingResponse>?>
}