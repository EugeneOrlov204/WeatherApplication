package com.eorlov.weatherapplication.repository

import com.eorlov.weatherapplication.model.Weather

interface WeatherRepository {
    suspend fun getAll() : List<Weather>
    suspend fun addHomeWeather(vararg weathers: Weather)
    suspend fun delete(weather: Weather)
    suspend fun insertAll(vararg weathers: Weather)
    suspend fun clearTable()
    suspend fun findWeatherByLocationName(location: String): Weather?
}