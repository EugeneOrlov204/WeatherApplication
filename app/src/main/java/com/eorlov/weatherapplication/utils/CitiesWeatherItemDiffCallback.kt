package com.eorlov.weatherapplication.utils

import androidx.recyclerview.widget.DiffUtil
import com.eorlov.weatherapplication.model.Weather

class CitiesWeatherItemDiffCallback : DiffUtil.ItemCallback<Weather>() {
    override fun areItemsTheSame(oldItem: Weather, newItem: Weather): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Weather, newItem: Weather): Boolean =
        oldItem == newItem
}