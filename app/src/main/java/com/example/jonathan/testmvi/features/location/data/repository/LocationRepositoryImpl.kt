package com.example.jonathan.testmvi.features.location.data.repository

import com.example.jonathan.testmvi.features.location.data.datasource.platform.LocationDataSource
import com.example.jonathan.testmvi.features.location.data.dto.LocationDataMapper.toEntity
import com.example.jonathan.testmvi.features.location.domain.entity.LocationEntity

/**
 * Converts raw DTO data into LocationEntity.
 */
class LocationRepositoryImpl(
    private val dataSource: LocationDataSource
) : LocationRepository {

    override suspend fun getCurrentLocation(): LocationEntity? {
        val triple = dataSource.getCurrentLocation() ?: return null
        val (dto, speedMph, locationName) = triple
        return dto.toEntity(speedMph, locationName)
    }
}
