package com.example.jonathan.testmvi.shared.domain.location.entity

import kotlin.math.abs

data class LocationEntity(
    val latitude: Double,
    val longitude: Double,
    val speedMph: Float,
    val locationName: String? = null
) {
    fun isSameAs(other: LocationEntity, epsilon: Double = 0.0001, floatEpsilon: Float = 0.01f):
            Boolean {
        return abs(x = latitude - other.latitude) < epsilon &&
                abs(longitude - other.longitude) < epsilon &&
                abs(speedMph - other.speedMph) < floatEpsilon &&
                locationName == other.locationName
    }
}