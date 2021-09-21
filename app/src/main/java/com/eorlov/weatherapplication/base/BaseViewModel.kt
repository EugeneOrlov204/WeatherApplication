package com.eorlov.weatherapplication.base

import androidx.lifecycle.ViewModel
import com.eorlov.weatherapplication.api.WeatherResponse
import com.eorlov.weatherapplication.model.Weather
import com.eorlov.weatherapplication.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

abstract class BaseViewModel : ViewModel() {
    private fun getDate(): String {
        return "${
            SimpleDateFormat(
                "EEEE", Locale.ENGLISH
            ).format(Calendar.getInstance().time.time)
        }, ${SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).format(Date())}"
    }

    private fun getTime(): String {
        return SimpleDateFormat("hh:mm", Locale.ENGLISH).format(Date())
    }

    private fun getTemperature(temperatureInKelvin: Float): String {
        return "${(temperatureInKelvin.plus(Constants.NUMBER_OF_DEGREES_KELVIN_IN_CELSIUS).toInt())}°"
    }

    private fun getWeatherType(type: String): String {
        return "http://openweathermap.org/img/wn/${type}@2x.png"
    }

    protected fun createWeather(response: WeatherResponse) = Weather(
        locationName = response.name ?: "",
        temperature = getTemperature(response.main?.temp ?: 0f),
        weatherType = getWeatherType(response.weather[0].icon ?: ""),
        latitude = response.coord?.lat ?: 0f,
        longitude = response.coord?.lon ?: 0f,
        date = getDate(),
        time = getTime(),
        humidity = "${response.main?.humidity?.toInt()}%",
        windSpeed = "${response.wind?.speed} m/sec"
    )
}