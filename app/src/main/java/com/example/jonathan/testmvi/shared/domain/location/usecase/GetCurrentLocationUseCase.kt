package com.example.jonathan.testmvi.shared.domain.location.usecase

import com.example.jonathan.testmvi.shared.domain.location.entity.LocationEntity
import com.example.jonathan.testmvi.features.location.data.repository.LocationRepository

class GetCurrentLocationUseCase(private val repository: LocationRepository) {
    suspend operator fun invoke(): LocationEntity? = repository.getCurrentLocation()
}
