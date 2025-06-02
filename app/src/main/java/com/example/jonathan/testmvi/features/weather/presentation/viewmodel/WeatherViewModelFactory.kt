package com.example.jonathan.testmvi.features.weather.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jonathan.testmvi.features.location.domain.usecase.GetCurrentLocationUseCase
import com.example.jonathan.testmvi.features.weather.domain.usecase.GetLastWeatherLocationUseCase
import com.example.jonathan.testmvi.features.weather.domain.usecase.GetWeatherByLocationUseCase
import com.example.jonathan.testmvi.features.weather.domain.usecase.StoreLastWeatherLocationUseCase

/**
 * Creates WeatherViewModel with required use cases.
 */
class WeatherViewModelFactory(
    private val getWeatherByLocationUseCase: GetWeatherByLocationUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val getLastWeatherLocationUseCase: GetLastWeatherLocationUseCase,
    private val storeLastWeatherLocationUseCase: StoreLastWeatherLocationUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(
                getWeatherByLocationUseCase,
                getCurrentLocationUseCase,
                getLastWeatherLocationUseCase,
                storeLastWeatherLocationUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
