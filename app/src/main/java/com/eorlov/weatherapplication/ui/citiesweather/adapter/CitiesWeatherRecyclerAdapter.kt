package com.eorlov.weatherapplication.ui.citiesweather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.eorlov.weatherapplication.databinding.WeatherListItemBinding
import com.eorlov.weatherapplication.model.Weather
import com.eorlov.weatherapplication.ui.citiesweather.adapter.viewholder.CitiesViewHolder
import com.eorlov.weatherapplication.utils.CitiesWeatherItemDiffCallback

class CitiesWeatherRecyclerAdapter :
    ListAdapter<Weather, CitiesViewHolder>(CitiesWeatherItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        return CitiesViewHolder(
            WeatherListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun getItemId(position: Int): Long = position.toLong()
}
