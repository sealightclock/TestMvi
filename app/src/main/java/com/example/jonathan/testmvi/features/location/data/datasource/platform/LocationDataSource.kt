package com.example.jonathan.testmvi.features.location.data.datasource.platform

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import java.util.Locale
import com.example.jonathan.testmvi.features.location.data.repository.LocationRepository
import com.example.jonathan.testmvi.features.location.domain.entity.LocationEntity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Platform-specific implementation to fetch device location and reverse-geocode it into a readable place name.
 */
class LocationDataSource(
    private val context: Context
) : LocationRepository {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    /**
     * Retrieves the current location and converts it into a LocationEntity, including a city/state/country string.
     * Note: Caller must ensure that location permission has already been granted.
     */
    @SuppressLint("MissingPermission") // We assume permission is already granted via LocationPermissionGate
    override suspend fun getCurrentLocation(): LocationEntity? {
        val location = fusedLocationClient.lastLocation.await() ?: return null

        val speedMph = location.speed * 2.23694f // Convert m/s to mph

        // Try to convert latitude/longitude into a readable location string (e.g., Irvine, CA, US)
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

        return LocationEntity(
            latitude = location.latitude,
            longitude = location.longitude,
            speedMph = speedMph,
            locationName = locationName
        )
    }
}


