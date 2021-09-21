package com.eorlov.weatherapplication.repository

import com.eorlov.weatherapplication.db.WeatherDao
import com.eorlov.weatherapplication.model.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val weatherDao: WeatherDao) :
    WeatherRepository {
    override suspend fun getAll(): List<Weather> {
        var listOfWeathers: List<Weather>
        withContext(Dispatchers.IO) {
            listOfWeathers = weatherDao.getAll()
        }
        return listOfWeathers
    }

    override suspend fun addHomeWeather(vararg weathers: Weather) {
        insertAll(*weathers)
    }

    override suspend fun delete(weather: Weather) {
        weatherDao.delete(weather)
    }

    override suspend fun insertAll(vararg weathers: Weather) {
        weatherDao.insertAll(*weathers)
    }

    override suspend fun clearTable() {
        weatherDao.clearTable()
    }

    override suspend fun findWeatherByLocationName(location: String): Weather? {
        var weather: Weather?
        withContext(Dispatchers.IO) {
            weather = weatherDao.findByLocationName(location)
        }
        return weather
    }
}