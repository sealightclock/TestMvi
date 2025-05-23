package com.example.jonathan.testmvi.features.location.data.repository

import android.annotation.SuppressLint
import android.content.Context
import com.example.jonathan.testmvi.features.location.domain.entity.LocationEntity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

class LocationRepositoryImpl(context: Context) : LocationRepository {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): LocationEntity? {
        val location = fusedLocationClient.lastLocation.await() ?: return null
        val speedMps = location.speed
        val speedMph = speedMps * 2.23694f
        return LocationEntity(location.latitude, location.longitude, speedMph)
    }
}
