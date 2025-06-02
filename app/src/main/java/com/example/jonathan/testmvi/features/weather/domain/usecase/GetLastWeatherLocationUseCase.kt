package com.example.jonathan.testmvi.features.weather.domain.usecase

import com.example.jonathan.testmvi.features.weather.data.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class GetLastWeatherLocationUseCase(
    private val repository: WeatherRepository
) {
    fun execute(): Flow<String?> = repository.getLastLocationFlow()
}
