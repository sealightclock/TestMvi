package com.example.jonathan.testmvi.features.location.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jonathan.testmvi.features.location.presentation.viewmodel.LocationViewModel
import com.example.jonathan.testmvi.features.location.presentation.viewmodel.LocationViewModelFactory
import com.example.jonathan.testmvi.shared.permission.LocationPermissionGate
import com.example.jonathan.testmvi.shared.preferences.PermissionPreferences
import com.example.jonathan.testmvi.shared.ui.CommonTopBar
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen() {
    val context = LocalContext.current

    val hasRequestedFineLocation by produceState<Boolean?>(initialValue = null, context) {
        value = PermissionPreferences.hasRequestedFineLocation(context).first()
    }
    val hasRequestedBackgroundLocation by produceState<Boolean?>(initialValue = null, context) {
        value = PermissionPreferences.hasRequestedBackgroundLocation(context).first()
    }

    Scaffold(
        topBar = { CommonTopBar(title = "Location") }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Always render the gate to maintain stable layout
            LocationPermissionGate {
                if (hasRequestedFineLocation != null && hasRequestedBackgroundLocation != null) {
                    LocationScreenContent()
                } else {
                    // Inline loader to keep layout stable
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp), // reserve vertical space
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationScreenContent() {
    val context = LocalContext.current.applicationContext

    val factory = remember { LocationViewModelFactory(context) }
    val viewModel: LocationViewModel = viewModel(factory = factory)

    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Location & Speed", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        state.locationName?.let {
            Text("Location: $it")
            Spacer(modifier = Modifier.height(8.dp))
        }

        state.latitude?.let { Text("Latitude: $it") }
        state.longitude?.let { Text("Longitude: $it") }
        state.speedMph?.let { Text("Speed: ${"%.2f".format(it)} mph") }

        // ✅ Show spinner only while loading AND no location has been shown yet
        if (state.isLoading && state.latitude == null && state.longitude == null) {
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.height(40.dp)) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        state.error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
        }
    }
}
