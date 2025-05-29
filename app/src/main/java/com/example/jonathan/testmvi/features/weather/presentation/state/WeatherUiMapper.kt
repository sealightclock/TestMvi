package com.example.jonathan.testmvi.features.weather.presentation.state

import com.example.jonathan.testmvi.features.weather.domain.entity.WeatherEntity

/**
 * Maps domain WeatherEntity into WeatherState (UI layer).
 */
object WeatherUiMapper {
    fun toState(
        entity: WeatherEntity,
        currentLocationInput: String
    ): WeatherState {
        return WeatherState(
            currentLocationInput = currentLocationInput,
            resolvedLocation = entity.locationName,
            temperature = entity.temperatureFahrenheit,
            isLoading = false,
            error = null
        )
    }
}
