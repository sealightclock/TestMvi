package com.example.jonathan.testmvi.features.location.presentation.state

import com.example.jonathan.testmvi.features.location.domain.entity.LocationEntity

/**
 * A mapper class to convert between UI state (LocationState) and domain entity (LocationEntity).
 * This keeps our architecture clean by avoiding direct conversion logic in the ViewModel.
 */
object LocationUiMapper {

    // Convert UI state to domain entity (for comparing or processing in domain layer)
    fun toEntity(state: LocationState): LocationEntity? {
        val lat = state.latitude
        val lng = state.longitude
        val speed = state.speedMph
        val name = state.locationName
        return if (lat != null && lng != null && speed != null) {
            LocationEntity(lat, lng, speed, name)
        } else {
            null
        }
    }

    // Convert domain entity to UI state (for rendering)
    fun toState(entity: LocationEntity): LocationState {
        return LocationState(
            latitude = entity.latitude,
            longitude = entity.longitude,
            speedMph = entity.speedMph,
            locationName = entity.locationName,
            isLoading = false,
            error = null
        )
    }
}
