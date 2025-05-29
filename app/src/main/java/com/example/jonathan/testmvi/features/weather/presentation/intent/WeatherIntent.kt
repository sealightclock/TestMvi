package com.example.jonathan.testmvi.features.weather.presentation.intent

/**
 * Represents all user interactions with the Weather screen.
 */
sealed class WeatherIntent {
    object LoadWeatherFromCurrentLocation : WeatherIntent()
    data class UpdateLocationInput(val input: String) : WeatherIntent()
    data class SubmitLocation(val input: String) : WeatherIntent()
}
