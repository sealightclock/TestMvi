package com.example.jonathan.testmvi.features.weather.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jonathan.testmvi.features.location.domain.usecase.GetCurrentLocationUseCase
import com.example.jonathan.testmvi.features.weather.domain.usecase.GetWeatherByLocationUseCase
import com.example.jonathan.testmvi.features.weather.presentation.intent.WeatherIntent
import com.example.jonathan.testmvi.features.weather.presentation.state.WeatherState
import com.example.jonathan.testmvi.features.weather.presentation.state.WeatherUiMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel that handles weather-related business logic and state.
 */
class WeatherViewModel(
    private val getWeatherByLocationUseCase: GetWeatherByLocationUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state

    fun handleIntent(intent: WeatherIntent) {
        when (intent) {
            is WeatherIntent.LoadWeatherFromCurrentLocation -> loadFromCurrentLocation()
            is WeatherIntent.UpdateLocationInput -> {
                _state.value = _state.value.copy(currentLocationInput = intent.input)
            }
            is WeatherIntent.SubmitLocation -> {
                loadFromLocationInput(intent.input)
            }
        }
    }

    private fun loadFromCurrentLocation() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val location = getCurrentLocationUseCase()
                if (location == null) {
                    _state.value = _state.value.copy(isLoading = false, error = "Unable to get location.")
                    return@launch
                }

                val fullLocation = location.locationName ?: "${location.latitude}, ${location.longitude}"
                val weather = getWeatherByLocationUseCase(fullLocation)
                _state.value = WeatherUiMapper.toState(weather, fullLocation)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun loadFromLocationInput(input: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val weather = getWeatherByLocationUseCase(input)
                _state.value = WeatherUiMapper.toState(weather, input)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}
