package com.example.jonathan.testmvi.features.location.presentation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.jonathan.testmvi.features.location.data.repository.LocationRepositoryImpl
import com.example.jonathan.testmvi.features.location.domain.usecase.GetCurrentLocationUseCase
import com.example.jonathan.testmvi.features.location.presentation.viewmodel.LocationViewModel

@Composable
fun LocationScreen() {
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
