package com.eorlov.weatherapplication.ui.citiesweather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eorlov.weatherapplication.api.GeocodingResponse
import com.eorlov.weatherapplication.api.WeatherResponse
import com.eorlov.weatherapplication.base.BaseViewModel
import com.eorlov.weatherapplication.model.Weather
import com.eorlov.weatherapplication.repository.MainRepository
import com.eorlov.weatherapplication.repository.WeatherRepositoryImpl
import com.eorlov.weatherapplication.utils.Constants.APP_ID
import com.eorlov.weatherapplication.utils.Constants.HOMETOWN
import com.eorlov.weatherapplication.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CitiesWeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepositoryImpl,
    private val repository: MainRepository
) :
    BaseViewModel() {

    val citiesWeatherListLiveData = MutableLiveData<MutableList<Weather>>(ArrayList())
    val loadEvent = MutableLiveData<Results>()

    fun initializeData() {
        viewModelScope.launch {
            if (citiesWeatherListLiveData.value == null) {
                loadEvent.postValue(Results.INITIALIZE_DATA_ERROR)
            } else {
                loadEvent.postValue(Results.LOADING)
                val data = getAll()
                if (data.isNotEmpty()) {
                    loadEvent.postValue(Results.OK)
                    citiesWeatherListLiveData.postValue(data.toMutableList())
                } else {
                    setDefaultWeather()
                }
            }
        }
    }

    fun getItem(position: Int): Weather? {
        return citiesWeatherListLiveData.value?.get(position)
    }

    suspend fun removeItem(position: Int) {
        weatherRepository.delete(
            citiesWeatherListLiveData.value?.get(position) ?: return
        )
        citiesWeatherListLiveData.value?.removeAt(position)
        citiesWeatherListLiveData.value = citiesWeatherListLiveData.value
    }

    suspend fun addItem(position: Int, item: Weather) {
        if (isNotExistingWeather(item)) {
            weatherRepository.insertAll(item)
            citiesWeatherListLiveData.value?.add(position, item)
            citiesWeatherListLiveData.value = citiesWeatherListLiveData.value
        }
    }

    suspend fun getAll() = weatherRepository.getAll()

    suspend fun refreshItem(refreshedItems: List<Weather>) {
        try {
            weatherRepository.clearTable()
            val listOfWeathers = mutableListOf<Weather>()
            for (item in refreshedItems) {
                val listOfLocations = getCityLocation(item.locationName)

                val response: WeatherResponse = repository.getCurrentWeatherData(
                    listOfLocations?.get(0)?.lat.toString(),
                    listOfLocations?.get(0)?.lon.toString(),
                    APP_ID
                ) ?: return

                listOfWeathers.add(createWeather(response))
            }
            weatherRepository.insertAll(*listOfWeathers.toTypedArray())
        } catch (exception: IOException) {
            loadEvent.value = Results.INTERNET_ERROR
        }
    }

    private suspend fun isNotExistingWeather(weather: Weather): Boolean {
        return weatherRepository.findWeatherByLocationName(weather.locationName) == null
    }

    private suspend fun setDefaultWeather() {
        val listOfLocations = getCityLocation(HOMETOWN)
        if (listOfLocations?.isEmpty() == true) return
        val response: WeatherResponse = repository.getCurrentWeatherData(
            listOfLocations?.get(0)?.lat.toString(),
            listOfLocations?.get(0)?.lon.toString(),
            APP_ID
        ) ?: return

        val weather = createWeather(response)
        citiesWeatherListLiveData.postValue(mutableListOf(weather))
        weatherRepository.addHomeWeather(weather)
    }

    private suspend fun getCityLocation(cityName: String): ArrayList<GeocodingResponse>? {
        return repository.getLocation(cityName, APP_ID)
    }
}