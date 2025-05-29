package com.example.jonathan.testmvi.features.weather.data.datasource

import android.util.Log
import com.example.jonathan.testmvi.features.weather.data.dto.WeatherDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Handles communication with the OpenWeatherMap API.
 */
class WeatherApiDataSource(
    apiKey: String
) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true // Ignore extra fields not in DTO
            })
        }
    }

    private val key = apiKey

    suspend fun fetchWeather(locationName: String): WeatherDto {
        return try {
            val response: HttpResponse = client.get("https://api.openweathermap.org/data/2.5/weather") {
                parameter("q", locationName)
                parameter("appid", key)
                parameter("units", "imperial")
            }

            if (response.status == HttpStatusCode.OK) {
                response.body()
            } else {
                throw Exception("API error: ${response.status}")
            }
        } catch (e: Exception) {
            Log.e("WeatherApiDataSource", "Error fetching weather: ${e.message}")
            throw e
        }
    }
}
