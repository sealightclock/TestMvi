package com.example.jonathan.testmvi.features.weather.data.repository

import com.example.jonathan.testmvi.features.weather.domain.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeatherByLocationName(locationName: String): WeatherEntity
    suspend fun saveLastLocation(location: String)
    fun getLastLocationFlow(): Flow<String?>
}
