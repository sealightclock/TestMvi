package com.example.jonathan.testmvi.features.weather.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jonathan.testmvi.features.location.domain.usecase.GetCurrentLocationUseCase
import com.example.jonathan.testmvi.features.weather.domain.usecase.GetWeatherByLocationUseCase

/**
 * Creates WeatherViewModel with required dependencies.
 */
class WeatherViewModelFactory(
    private val context: Context,
    private val getWeatherByLocationUseCase: GetWeatherByLocationUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherViewModel(context,
            getWeatherByLocationUseCase,
            getCurrentLocationUseCase) as T
    }
}
