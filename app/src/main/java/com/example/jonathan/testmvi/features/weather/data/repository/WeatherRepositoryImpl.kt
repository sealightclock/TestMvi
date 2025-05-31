package com.example.jonathan.testmvi.features.weather.data.repository

import com.example.jonathan.testmvi.features.weather.data.datasource.remote.WeatherKtorDataSource
import com.example.jonathan.testmvi.features.weather.data.dto.WeatherDataMapper
import com.example.jonathan.testmvi.features.weather.domain.entity.WeatherEntity

/**
 * Concrete implementation of WeatherRepository using OpenWeatherMap API.
 */
class WeatherRepositoryImpl(
    private val apiDataSource: WeatherKtorDataSource
) : WeatherRepository {

    override suspend fun getWeatherByLocationName(locationName: String): WeatherEntity {
        val dto = apiDataSource.fetchWeather(locationName)
        return WeatherDataMapper.toEntity(dto)
    }
}
