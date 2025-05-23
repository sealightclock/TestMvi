package com.example.jonathan.testmvi.features.location.presentation.view

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.jonathan.testmvi.features.location.data.repository.LocationRepositoryImpl
import com.example.jonathan.testmvi.features.location.domain.usecase.GetCurrentLocationUseCase
import com.example.jonathan.testmvi.features.location.presentation.viewmodel.LocationViewModel
import com.example.jonathan.testmvi.shared.permission.PermissionGate

@Composable
fun LocationScreen() {
    PermissionGate(
        permission = Manifest.permission.ACCESS_FINE_LOCATION,
        rationaleMessage = "Location permission is needed to display your current location and speed.",
        permanentlyDeniedMessage = "Location permission is permanently denied. Please enable it in App Settings."
    ) {
        LocationScreenContent()
    }
}

@Composable
private fun LocationScreenContent() {
    val context = LocalContext.current.applicationContext

    val viewModel: LocationViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                val repo = LocationRepositoryImpl(context)
                val useCase = GetCurrentLocationUseCase(repo)
                LocationViewModel(useCase)
            }
        }
    )

    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Location & Speed", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        state.latitude?.let { Text("Latitude: $it") }
        state.longitude?.let { Text("Longitude: $it") }
        state.speedMph?.let { Text("Speed: ${"%.2f".format(it)} mph") }

        if (state.isLoading) {
            Spacer(modifier = Modifier.height(8.dp))
            CircularProgressIndicator()
        }

        state.error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
        }
    }
}
