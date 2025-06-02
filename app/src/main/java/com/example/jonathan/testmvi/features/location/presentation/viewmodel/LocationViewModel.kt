package com.example.jonathan.testmvi.features.location.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jonathan.testmvi.shared.domain.location.usecase.GetCurrentLocationUseCase
import com.example.jonathan.testmvi.features.location.presentation.intent.LocationIntent
import com.example.jonathan.testmvi.features.location.presentation.state.LocationState
import com.example.jonathan.testmvi.features.location.presentation.state.LocationUiMapper
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
            is LocationIntent.LoadLocation -> loadLocation()
        }
    }

    private fun loadLocation() {
        viewModelScope.launch {
            while (true) {
                _state.value = _state.value.copy(isLoading = true, error = null)

                try {
                    val location = getCurrentLocationUseCase.invoke()

                    if (location != null) {
                        val current = _state.value
                        val currentEntity = LocationUiMapper.toEntity(current)

                        if (currentEntity == null || !location.isSameAs(currentEntity)) {
                            _state.value = LocationUiMapper.toState(location)
                        } else {
                            _state.value = current.copy(isLoading = false)
                        }
                    } else {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = "Location unavailable"
                        )
                    }
                } catch (e: Exception) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }

                delay(1000)
            }
        }
    }
}
