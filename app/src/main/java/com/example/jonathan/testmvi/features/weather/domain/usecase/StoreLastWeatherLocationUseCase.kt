package com.example.jonathan.testmvi.features.weather.domain.usecase

import com.example.jonathan.testmvi.features.weather.data.repository.WeatherRepository

class StoreLastWeatherLocationUseCase(
    private val repository: WeatherRepository
) {
    suspend fun execute(location: String) = repository.saveLastLocation(location)
}
