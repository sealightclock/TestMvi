package com.example.jonathan.testmvi.features.location.presentation.state

data class LocationState(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val speedMph: Float? = null,
    val locationName: String? = null, // ✅ new field
    val isLoading: Boolean = false,
    val error: String? = null
)
