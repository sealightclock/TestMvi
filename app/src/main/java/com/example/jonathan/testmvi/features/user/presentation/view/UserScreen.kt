package com.example.jonathan.testmvi.features.user.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    // Get the lifecycle owner of this Composable
    val lifecycleOwner = LocalLifecycleOwner.current

    // Create a lifecycle-aware version of the ViewModel's userState flow
    val lifecycleAwareFlow = remember(viewModel, lifecycleOwner) {
        viewModel.userState.flowWithLifecycle(
            lifecycle = lifecycleOwner.lifecycle,
            minActiveState = Lifecycle.State.STARTED
        )
    }

    // Collect the flow into a Compose state variable
    val state by lifecycleAwareFlow.collectAsState(initial = UserState())

    // Create a coroutine scope for dispatching intents
    val scope = rememberCoroutineScope()

    // === Extract intent dispatch logic into local lambdas ===

    // Lambda to update name (must return (String) -> Unit)
    val onNameChange: (String) -> Unit = remember(viewModel, scope) {
        { name ->
            scope.launch {
                viewModel.handleIntent(UserIntent.UpdateName(name))
            }
        }
    }

    // Lambda to update age
    val onAgeChange: (String) -> Unit = remember(viewModel, scope) {
        { age ->
            scope.launch {
                viewModel.handleIntent(UserIntent.UpdateAge(age))
            }
        }
    }

    // Lambda to load user data
    val onLoadUser: () -> Unit = remember(viewModel, scope) {
        {
            scope.launch {
                viewModel.handleIntent(UserIntent.LoadUser)
            }
        }
    }

    // === UI layout starts here ===

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            // Name input field (value: String, onValueChange: (String) -> Unit)
            TextField(
                value = state.name,
                onValueChange = onNameChange,
                label = { Text("Name") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Age input field
            TextField(
                value = state.age,
                onValueChange = onAgeChange,
                label = { Text("Age") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Load User button
            Button(onClick = onLoadUser) {
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
