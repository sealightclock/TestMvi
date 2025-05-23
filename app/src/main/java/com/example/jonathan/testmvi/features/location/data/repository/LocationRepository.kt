package com.example.jonathan.testmvi.features.location.data.repository

import com.example.jonathan.testmvi.features.location.domain.entity.LocationEntity

interface LocationRepository {
    suspend fun getCurrentLocation(): LocationEntity?
}
