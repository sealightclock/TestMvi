package com.example.jonathan.testmvi.features.weather.data.repository

import com.example.jonathan.testmvi.features.weather.data.datasource.local.WeatherDataStoreDataSource
import com.example.jonathan.testmvi.features.weather.data.datasource.remote.WeatherKtorDataSource
import com.example.jonathan.testmvi.features.weather.data.dto.WeatherDataMapper
import com.example.jonathan.testmvi.features.weather.domain.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImpl(
    private val apiDataSource: WeatherKtorDataSource,
    private val localStore: WeatherDataStoreDataSource
) : WeatherRepository {

    override suspend fun getWeatherByLocationName(locationName: String): WeatherEntity {
        val dto = apiDataSource.fetchWeather(locationName)
        return WeatherDataMapper.toEntity(dto)
    }

    override suspend fun saveLastLocation(location: String) {
        localStore.saveLastLocation(location)
    }

    override fun getLastLocationFlow(): Flow<String?> {
        return localStore.lastLocationFlow()
    }
}
