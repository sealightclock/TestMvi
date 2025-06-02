package com.example.jonathan.testmvi.features.location.data.repository

import com.example.jonathan.testmvi.shared.domain.location.entity.LocationEntity

interface LocationRepository {
    suspend fun getCurrentLocation(): LocationEntity?
}
