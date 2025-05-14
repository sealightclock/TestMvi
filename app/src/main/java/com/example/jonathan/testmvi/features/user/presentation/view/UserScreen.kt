package com.example.jonathan.testmvi.features.user.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jonathan.testmvi.features.user.presentation.intent.UserIntent
import com.example.jonathan.testmvi.features.user.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun UserScreen(viewModel: UserViewModel) {
    val state by viewModel.userState.collectAsState()

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            TextField(
                value = state.name,
                onValueChange = { scope.launch { viewModel.handleIntent(UserIntent.UpdateName(it)) } },
                label = { Text("Name") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = state.age.toString(),
                onValueChange = { scope.launch { viewModel.handleIntent(UserIntent.UpdateAge(
                    if (it.isBlank()) 0 else it.toIntOrNull() ?: 0)) } },
                label = { Text("Age") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { scope.launch { viewModel.handleIntent(UserIntent.LoadUser) } }) {
                Text("Load User")
            }
        }

        state.error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Error: $it")
        }
    }
}
