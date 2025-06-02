package com.example.jonathan.testmvi.features.weather.data.datasource.local

/**
 * Provides a higher-level DataSource abstraction over DataStore API.
 */
class WeatherDataStoreDataSource(private val api: WeatherDataStoreApi) {

    fun lastLocationFlow() = api.lastLocationFlow()

    suspend fun saveLastLocation(location: String) {
        api.saveLastLocation(location)
    }

//    suspend fun loadLastLocation(): String? {
//        return api.loadLastLocation()
//    }
}
