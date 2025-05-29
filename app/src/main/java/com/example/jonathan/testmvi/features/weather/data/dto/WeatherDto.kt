package com.example.jonathan.testmvi.features.weather.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO representing the response from OpenWeatherMap API.
 */
@Serializable
data class WeatherDto(
    @SerialName("name")
    val cityName: String,

    @SerialName("main")
    val main: MainDto
)

@Serializable
data class MainDto(
    @SerialName("temp")
    val temperature: Float // Temperature in Fahrenheit (if units=imperial)
)
