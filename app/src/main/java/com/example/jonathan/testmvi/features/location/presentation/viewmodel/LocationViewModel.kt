package com.example.jonathan.testmvi.features.location.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jonathan.testmvi.features.location.presentation.intent.LocationIntent
import com.example.jonathan.testmvi.features.location.presentation.state.LocationState
import com.example.jonathan.testmvi.features.location.domain.usecase.GetCurrentLocationUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LocationViewModel(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(LocationState())
    val state: StateFlow<LocationState> = _state

    init {
        handleIntent(LocationIntent.LoadLocation)
    }

    fun handleIntent(intent: LocationIntent) {
        when (intent) {
            is LocationIntent.LoadLocation -> startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        viewModelScope.launch {
            while (true) {
                _state.value = _state.value.copy(isLoading = true, error = null)
                try {
                    val location = getCurrentLocationUseCase()
                    if (location != null) {
                        _state.value = LocationState(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            speedMph = location.speedMph,
                            isLoading = false
                        )
                    } else {
                        _state.value = _state.value.copy(isLoading = false, error = "Location unavailable")
                    }
                } catch (e: Exception) {
                    _state.value = _state.value.copy(isLoading = false, error = e.message)
                }
                delay(1000)
            }
        }
    }
}
