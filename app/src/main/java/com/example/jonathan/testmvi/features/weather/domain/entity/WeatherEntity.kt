package com.example.jonathan.testmvi.features.weather.domain.entity

/**
 * Represents a simple weather data model used across domain and UI layers.
 */
data class WeatherEntity(
    val locationName: String, // e.g., "Irvine, California, US"
    val temperatureFahrenheit: String // e.g., "72°F"
)
