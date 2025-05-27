
package com.example.jonathan.testmvi.features.location.domain.entity

data class LocationEntity(
    val latitude: Double,
    val longitude: Double,
    val speedMph: Float,
    val locationName: String? = null
) {
    fun isSameAs(other: LocationEntity, epsilon: Double = 0.0001, floatEpsilon: Float = 0.01f):
            Boolean {
        return kotlin.math.abs(x = latitude - other.latitude) < epsilon &&
                kotlin.math.abs(longitude - other.longitude) < epsilon &&
                kotlin.math.abs(speedMph - other.speedMph) < floatEpsilon &&
                locationName == other.locationName
    }
}