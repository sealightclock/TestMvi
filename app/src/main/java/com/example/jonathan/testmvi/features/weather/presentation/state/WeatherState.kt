package com.example.jonathan.testmvi.features.weather.presentation.state

/**
 * Represents the full state of the Weather screen UI.
 */
data class WeatherState(
    val currentLocationInput: String = "",         // Input text field value
    val resolvedLocation: String = "",             // Resolved from API or GPS
    val temperature: String = "",                  // e.g., "72°F"
    val isLoading: Boolean = false,
    val error: String? = null
)
