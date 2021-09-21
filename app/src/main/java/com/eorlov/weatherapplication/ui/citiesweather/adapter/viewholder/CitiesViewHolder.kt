package com.eorlov.weatherapplication.ui.citiesweather.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.eorlov.weatherapplication.databinding.WeatherListItemBinding
import com.eorlov.weatherapplication.model.Weather

class CitiesViewHolder(
    private val binding: WeatherListItemBinding,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindTo(weatherModel: Weather) {
        binding.apply {
            simpleDraweeViewWeather.setImageURI(weatherModel.weatherType)
            textViewCityName.text = weatherModel.locationName
            textViewTemperature.text = weatherModel.temperature
        }
    }
}