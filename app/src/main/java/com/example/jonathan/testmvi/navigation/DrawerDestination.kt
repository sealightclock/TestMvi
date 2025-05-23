package com.example.jonathan.testmvi.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class DrawerDestination(val route: String, val label: String, val icon: ImageVector) {
    object Users : DrawerDestination("users", "Users", Icons.Default.Person)
    object Settings : DrawerDestination("settings", "Settings", Icons.Default.Settings)
    object Location : DrawerDestination("location", "Location", Icons.Default.LocationOn)

    companion object {
        val all = listOf(Users, Settings, Location)
    }
}

