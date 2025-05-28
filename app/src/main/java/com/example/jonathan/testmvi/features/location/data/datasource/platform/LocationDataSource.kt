// features/location/data/datasource/platform/LocationDataSource.kt
package com.example.jonathan.testmvi.features.location.data.datasource.platform

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.example.jonathan.testmvi.features.location.data.dto.LocationDto
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale

/**
 * Platform-specific implementation to fetch device location and reverse-geocode it.
 * Returns DTO + speed + location name — the Repository will map to LocationEntity.
 */
class LocationDataSource(
    private val context: Context
) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    /**
     * Caller must ensure permission is already granted.
     */
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Triple<LocationDto, Float, String?>? {
        val location = fusedLocationClient.lastLocation.await() ?: return null
        val speedMph = location.speed * 2.23694f // m/s to mph

        val locationName = try {
            withContext(Dispatchers.IO) {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                addresses?.firstOrNull()?.let {
                    listOfNotNull(it.locality, it.adminArea, it.countryCode).joinToString(", ")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Unknown location"
        }

        return Triple(
            LocationDto(latitude = location.latitude, longitude = location.longitude),
            speedMph,
            locationName
        )
    }
}
