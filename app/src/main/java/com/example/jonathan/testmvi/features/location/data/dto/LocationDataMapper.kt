package com.example.jonathan.testmvi.features.location.data.dto

import com.example.jonathan.testmvi.shared.domain.location.entity.LocationEntity

/**
 * Responsible for converting between DTOs and domain entities.
 */
object LocationDataMapper {
    fun LocationDto.toEntity(speedMph: Float, locationName: String?) = LocationEntity(
        longitude = longitude, latitude = latitude,
        speedMph = speedMph,
        locationName = locationName,
    )

    fun LocationEntity.toDto() = LocationDto(
        longitude = longitude,
        latitude = latitude)
}
