package com.example.jonathan.testmvi.features.weather.data.dto

import com.example.jonathan.testmvi.features.weather.domain.entity.WeatherEntity

/**
 * Converts WeatherDto (API model) to WeatherEntity (UI/Domain model).
 */
object WeatherDataMapper {
    fun toEntity(dto: WeatherDto): WeatherEntity {
        return WeatherEntity(
            locationName = dto.cityName,
            temperatureFahrenheit = "${dto.main.temperature.toInt()}°F"
        )
    }
}
