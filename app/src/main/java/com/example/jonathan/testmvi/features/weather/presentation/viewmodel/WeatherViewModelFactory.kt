package com.example.jonathan.testmvi.features.weather.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jonathan.testmvi.features.location.data.datasource.platform.LocationDataSource
import com.example.jonathan.testmvi.features.location.data.repository.LocationRepositoryImpl
import com.example.jonathan.testmvi.shared.domain.location.usecase.GetCurrentLocationUseCase
import com.example.jonathan.testmvi.features.weather.data.datasource.local.WeatherDataStoreDataSource
import com.example.jonathan.testmvi.features.weather.data.datasource.remote.WeatherKtorDataSource
import com.example.jonathan.testmvi.features.weather.data.repository.WeatherRepositoryImpl
import com.example.jonathan.testmvi.features.weather.domain.usecase.GetLastWeatherLocationUseCase
import com.example.jonathan.testmvi.features.weather.domain.usecase.GetWeatherByLocationUseCase
import com.example.jonathan.testmvi.features.weather.domain.usecase.StoreLastWeatherLocationUseCase

/**
 * Creates WeatherViewModel with full dependency setup.
 * Keeps View layer clean by hiding domain and data layer details.
 */
class WeatherViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }

        // Remote + Local DataSources
        val weatherRemote = WeatherKtorDataSource(apiKey = "cc9a943e9b0082101297ca40b03f1f83")
        val weatherLocal = WeatherDataStoreDataSource(context)

        // Repositories
        val weatherRepo = WeatherRepositoryImpl(weatherRemote, weatherLocal)
        val locationRepo = LocationRepositoryImpl(LocationDataSource(context))

        // Use Cases
        val getWeather = GetWeatherByLocationUseCase(weatherRepo)
        val getLocation = GetCurrentLocationUseCase(locationRepo)
        val getLast = GetLastWeatherLocationUseCase(weatherRepo)
        val storeLast = StoreLastWeatherLocationUseCase(weatherRepo)

        return WeatherViewModel(getWeather, getLocation, getLast, storeLast) as T
    }
}
