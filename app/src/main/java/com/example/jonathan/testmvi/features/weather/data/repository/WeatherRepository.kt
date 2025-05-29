package com.example.jonathan.testmvi.features.weather.data.repository

import com.example.jonathan.testmvi.features.weather.domain.entity.WeatherEntity

/**
 * Abstracts weather-related data access for the domain layer.
 */
interface WeatherRepository {
    suspend fun getWeatherByLocationName(locationName: String): WeatherEntity
}
