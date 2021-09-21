package com.eorlov.weatherapplication.repository

import com.eorlov.weatherapplication.api.MainRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val remoteData: MainRemoteData
) {
    suspend fun getCurrentWeatherData(lat: String?, lon: String?, app_id: String?) =
        remoteData.getCurrentWeatherData(lat, lon, app_id)

    suspend fun getLocation(cityName: String?, app_id: String?) =
        remoteData.getLocation(cityName, app_id)
}
