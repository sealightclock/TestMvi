package com.example.jonathan.testmvi.features.location.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jonathan.testmvi.features.location.domain.entity.LocationEntity
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
                        val current = _state.value

                        // Only update state if something changed
                        val currentEntity = current.toEntity()

                        if (currentEntity == null || !location.isSameAs(currentEntity)) {
                            _state.value = LocationState(
                                latitude = location.latitude,
                                longitude = location.longitude,
                                speedMph = location.speedMph,
                                locationName = location.locationName,
                                isLoading = false
                            )
                        } else {
                            _state.value = current.copy(isLoading = false)
                        }
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

    private fun LocationState.toEntity(): LocationEntity? {
        val lat = latitude
        val lng = longitude
        val speed = speedMph
        val name = locationName
        return if (lat != null && lng != null && speed != null) {
            LocationEntity(lat, lng, speed, name)
        } else {
            null
        }
    }
}
