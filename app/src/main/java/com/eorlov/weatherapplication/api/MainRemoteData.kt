package com.eorlov.weatherapplication.api

import javax.inject.Inject

class MainRemoteData @Inject constructor(private val mainService: MainService) {

    suspend fun getCurrentWeatherData(
        lat: String?,
        lon: String?,
        app_id: String?
    ): WeatherResponse? {
        with(
            mainService.getCurrentWeatherData(lat, lon, app_id)
        ) {
            return this?.body()
        }
    }

    suspend fun getLocation(cityName: String?, app_id: String?): ArrayList<GeocodingResponse>? {
        with(mainService.getLocation(cityName, app_id)) {
            return this.body()
        }
    }
}