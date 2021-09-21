package com.eorlov.weatherapplication.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eorlov.weatherapplication.model.Weather

@Database(entities = [Weather::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}