package com.example.jonathan.testmvi.features.weather.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jonathan.testmvi.features.weather.presentation.intent.WeatherIntent
import com.example.jonathan.testmvi.features.weather.presentation.viewmodel.WeatherViewModel
import com.example.jonathan.testmvi.features.weather.presentation.viewmodel.WeatherViewModelFactory
import com.example.jonathan.testmvi.shared.ui.CommonTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen() {
    val context = LocalContext.current.applicationContext

    val viewModel: WeatherViewModel = viewModel(
        factory = WeatherViewModelFactory(context)
    )

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(WeatherIntent.LoadWeatherFromSavedLocation)
    }

    Scaffold(topBar = {
        CommonTopBar(title = "Weather")
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = state.currentLocationInput,
                    onValueChange = {
                        viewModel.handleIntent(WeatherIntent.UpdateLocationInput(it))
                    },
                    label = { Text("Enter location (e.g., Boston, US)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.handleIntent(WeatherIntent.SubmitLocation(state.currentLocationInput))
                        }
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(text = "Location: ${state.resolvedLocation}")
                    Text(text = "Temperature: ${state.temperature}")
                }

                state.error?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Error: $it", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
