package com.example.jonathan.testmvi.features.location.presentation.view

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.jonathan.testmvi.features.location.data.repository.LocationRepositoryImpl
import com.example.jonathan.testmvi.features.location.domain.usecase.GetCurrentLocationUseCase
import com.example.jonathan.testmvi.features.location.presentation.viewmodel.LocationViewModel
import com.example.jonathan.testmvi.shared.permission.LocationPermissionGate
import com.example.jonathan.testmvi.shared.preferences.PermissionPreferences
import kotlinx.coroutines.flow.first

@Composable
fun LocationScreen() {
    val context = LocalContext.current

    // Preload DataStore values to avoid race conditions
    val hasRequestedFineLocation by produceState<Boolean?>(initialValue = null, context) {
        value = PermissionPreferences.hasRequestedFineLocation(context).first()
    }
    val hasRequestedBackgroundLocation by produceState<Boolean?>(initialValue = null, context) {
        value = PermissionPreferences.hasRequestedBackgroundLocation(context).first()
    }

    if (hasRequestedFineLocation != null && hasRequestedBackgroundLocation != null) {
        LocationPermissionGate {
            LocationScreenContent()
        }
    } else {
        // Show a progress indicator to prevent UI flicker
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
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
