package com.example.jonathan.testmvi.features.user.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.example.jonathan.testmvi.features.user.presentation.intent.UserIntent
import com.example.jonathan.testmvi.features.user.presentation.state.UserState
import com.example.jonathan.testmvi.features.user.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun UserScreen(viewModel: UserViewModel) {
    // Get the LifecycleOwner of the current Composable
    val lifecycleOwner = LocalLifecycleOwner.current

    // Convert the StateFlow into a Lifecycle-aware Flow:
    val lifecycleAwareFlow = remember(viewModel, lifecycleOwner) {
        viewModel.userState.flowWithLifecycle(
            lifecycle = lifecycleOwner.lifecycle,
            minActiveState = Lifecycle.State.STARTED // Only collect when screen is visible
        )
    }

    // Collect the state safely during composition (lifecycle-aware)
    val state by lifecycleAwareFlow.collectAsState(initial = UserState())

    // Create a coroutine scope for dispatching user intents
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            // Text field for name input
            TextField(
                value = state.name,
                onValueChange = { scope.launch { viewModel.handleIntent(UserIntent.UpdateName(it)) } },
                label = { Text("Name") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Text field for age input
            TextField(
                value = state.age,
                onValueChange = { scope.launch { viewModel.handleIntent(UserIntent.UpdateAge(it)) } },
                label = { Text("Age") }
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { scope.launch { viewModel.handleIntent(UserIntent.LoadUser) } }) {
                Text("Load User")
            }
        }

        // Show error message if any
        state.error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Error: $it", color = MaterialTheme.colorScheme.error)
        }
    }
}
