package com.example.jonathan.testmvi.shared.navigation

sealed class DrawerDestination(val route: String, val label: String) {
    object Users : DrawerDestination("users", "Users")
    object Settings : DrawerDestination("settings", "Settings")
    object Location : DrawerDestination("location", "Location")
    object Weather : DrawerDestination("weather", "Weather")

    companion object {
        fun fromRoute(route: String): DrawerDestination = when (route) {
            Users.route -> Users
            Settings.route -> Settings
            Location.route -> Location
            Weather.route -> Weather
            else -> Users // fallback
        }
    }
}
