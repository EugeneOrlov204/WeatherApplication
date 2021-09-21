package com.eorlov.weatherapplication.ui.homepage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eorlov.weatherapplication.model.Weather
import com.eorlov.weatherapplication.repository.WeatherRepositoryImpl
import com.eorlov.weatherapplication.utils.Constants.HOMETOWN
import com.eorlov.weatherapplication.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val weatherRepository: WeatherRepositoryImpl
) :
    ViewModel() {
    val homePageWeatherLiveData = MutableLiveData<Weather>()
    val loadEvent = MutableLiveData<Results>()

    suspend fun initializeData() {
        homePageWeatherLiveData.value = getWeather()
        if (homePageWeatherLiveData.value == null) {
            loadEvent.postValue(Results.INITIALIZE_DATA_ERROR)
        }
    }

    private suspend fun getWeather(): Weather? {
        return weatherRepository.findWeatherByLocationName(HOMETOWN)
    }
}