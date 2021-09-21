package com.eorlov.weatherapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Weather(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "location_name") val locationName: String,
    val temperature: String,
    @ColumnInfo(name = "weather_type") val weatherType: String?,
    val date: String = "",
    val time: String = "",
    val latitude: Float = 0.0f,
    val longitude: Float = 0.0f,
    val humidity: String = "",
    @ColumnInfo(name = "wind_speed") val windSpeed: String = ""
)