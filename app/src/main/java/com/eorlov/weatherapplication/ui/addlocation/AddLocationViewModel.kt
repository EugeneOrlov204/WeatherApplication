package com.eorlov.weatherapplication.ui.addlocation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eorlov.weatherapplication.api.WeatherResponse
import com.eorlov.weatherapplication.base.BaseViewModel
import com.eorlov.weatherapplication.model.Weather
import com.eorlov.weatherapplication.repository.MainRepository
import com.eorlov.weatherapplication.repository.WeatherRepositoryImpl
import com.eorlov.weatherapplication.utils.Constants.APP_ID
import com.eorlov.weatherapplication.utils.Results
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddLocationViewModel @Inject constructor(
    private val weatherRepository: WeatherRepositoryImpl,
    private val repository: MainRepository
) : BaseViewModel() {

    val cityLocationLiveData = MutableLiveData<LatLng?>()
    val loadEvent = MutableLiveData<Results>()
    val getWeatherLiveData = MutableLiveData<WeatherResponse>()

    suspend fun initCityLocation(cityName: String) {
        withContext(Dispatchers.IO) {
            val listOfLocations = repository.getLocation(cityName, APP_ID)
            if (listOfLocations?.isNotEmpty() == true) {
                cityLocationLiveData.postValue(
                    LatLng(
                        listOfLocations[0].lat?.toDouble() ?: 0.0,
                        listOfLocations[0].lon?.toDouble() ?: 0.0
                    )
                )
            } else {
                loadEvent.postValue(Results.NOT_EXISTED_CITY)
            }
        }
    }

    fun initWeather(lat: String?, lon: String?, app_id: String?) {
        viewModelScope.launch {
            getWeatherLiveData.postValue(
                repository.getCurrentWeatherData(
                    lat, lon, app_id
                )
            )
        }
    }

    fun getWeather (weatherResponse: WeatherResponse): Weather {
        return createWeather(weatherResponse)
    }

    suspend fun isNotExistingWeather(weather: Weather): Boolean {
        return weatherRepository.findWeatherByLocationName(weather.locationName) == null
    }

    suspend fun addItemToDatabase(weather: Weather) {
        weatherRepository.insertAll(weather)
    }
}