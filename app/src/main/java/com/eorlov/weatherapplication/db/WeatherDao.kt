package com.eorlov.weatherapplication.db

import androidx.room.*
import com.eorlov.weatherapplication.model.Weather

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather")
    suspend fun getAll(): List<Weather>

    @Query("SELECT * FROM weather WHERE location_name LIKE :location LIMIT 1")
    suspend fun findByLocationName(location: String): Weather

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg weathers: Weather)

    @Delete
    suspend fun delete(weather: Weather)

    @Query("DELETE FROM weather")
    suspend fun clearTable()
}