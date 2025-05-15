package com.example.jonathan.testmvi.features.users.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jonathan.testmvi.features.users.presentation.intent.UsersIntent
import com.example.jonathan.testmvi.features.users.presentation.state.UsersState
import com.example.jonathan.testmvi.features.users.presentation.viewmodel.UsersViewModel
import kotlinx.coroutines.launch

/**
 * Composable function for displaying the User screen.
 *
 * By default, the UserViewModel is created internally using the viewModel() delegate.
 * For testing or previews, you can optionally pass in a custom ViewModel instance.
 */
@Composable
fun UsersScreen(viewModel: UsersViewModel = viewModel()) {
    // Get the lifecycle owner of this Composable
    val lifecycleOwner = LocalLifecycleOwner.current

    // Convert the StateFlow into a Lifecycle-aware Flow
    val lifecycleAwareFlow = remember(viewModel, lifecycleOwner) {
        viewModel.userState.flowWithLifecycle(
            lifecycle = lifecycleOwner.lifecycle,
            minActiveState = Lifecycle.State.STARTED
        )
    }

    // Collect the state safely during composition (lifecycle-aware)
    val state by lifecycleAwareFlow.collectAsState(initial = UsersState())

    // Create a coroutine scope for dispatching user intents
    val scope = rememberCoroutineScope()

    // === Extract intent dispatch logic into lambdas ===

    val onNameChange: (String) -> Unit = remember(viewModel, scope) {
        { name ->
            scope.launch {
                viewModel.handleIntent(UsersIntent.UpdateName(name))
            }
        }
    }

    val onAgeChange: (String) -> Unit = remember(viewModel, scope) {
        { age ->
            scope.launch {
                viewModel.handleIntent(UsersIntent.UpdateAge(age))
            }
        }
    }

    val onLoadUser: () -> Unit = remember(viewModel, scope) {
        {
            scope.launch {
                viewModel.handleIntent(UsersIntent.LoadUser)
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
            // Name input field
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
