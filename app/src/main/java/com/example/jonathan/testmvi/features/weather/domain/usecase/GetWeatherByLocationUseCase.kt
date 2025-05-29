package com.example.jonathan.testmvi.features.weather.domain.usecase

import com.example.jonathan.testmvi.features.weather.data.repository.WeatherRepository
import com.example.jonathan.testmvi.features.weather.domain.entity.WeatherEntity

/**
 * Encapsulates the logic to fetch weather data for a given location.
 */
class GetWeatherByLocationUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(locationName: String): WeatherEntity {
        return repository.getWeatherByLocationName(locationName)
    }
}
