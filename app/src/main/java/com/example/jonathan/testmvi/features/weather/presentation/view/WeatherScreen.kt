package com.example.jonathan.testmvi.features.weather.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.jonathan.testmvi.features.location.data.datasource.platform.LocationDataSource
import com.example.jonathan.testmvi.features.location.data.repository.LocationRepositoryImpl
import com.example.jonathan.testmvi.features.location.domain.usecase.GetCurrentLocationUseCase
import com.example.jonathan.testmvi.features.weather.data.datasource.local.WeatherDataStoreDataSource
import com.example.jonathan.testmvi.features.weather.data.datasource.remote.WeatherKtorDataSource
import com.example.jonathan.testmvi.features.weather.data.repository.WeatherRepositoryImpl
import com.example.jonathan.testmvi.features.weather.domain.usecase.GetLastWeatherLocationUseCase
import com.example.jonathan.testmvi.features.weather.domain.usecase.GetWeatherByLocationUseCase
import com.example.jonathan.testmvi.features.weather.domain.usecase.StoreLastWeatherLocationUseCase
import com.example.jonathan.testmvi.features.weather.presentation.intent.WeatherIntent
import com.example.jonathan.testmvi.features.weather.presentation.viewmodel.WeatherViewModel
import com.example.jonathan.testmvi.features.weather.presentation.viewmodel.WeatherViewModelFactory
import com.example.jonathan.testmvi.shared.ui.CommonTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen() {
    val context = LocalContext.current.applicationContext
    val openWeatherApiKey = "cc9a943e9b0082101297ca40b03f1f83"

    val viewModel: WeatherViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                // Remote + Local DataSources
                val weatherRemote = WeatherKtorDataSource(openWeatherApiKey)
                val weatherLocal = WeatherDataStoreDataSource(context)

                // Repository with both sources
                val weatherRepo = WeatherRepositoryImpl(weatherRemote, weatherLocal)
                val locationRepo = LocationRepositoryImpl(LocationDataSource(context))

                // Use Cases
                val getWeather = GetWeatherByLocationUseCase(weatherRepo)
                val getLocation = GetCurrentLocationUseCase(locationRepo)
                val getLast = GetLastWeatherLocationUseCase(weatherRepo)
                val storeLast = StoreLastWeatherLocationUseCase(weatherRepo)

                WeatherViewModelFactory(getWeather, getLocation, getLast, storeLast)
                    .create(WeatherViewModel::class.java)
            }
        }
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
