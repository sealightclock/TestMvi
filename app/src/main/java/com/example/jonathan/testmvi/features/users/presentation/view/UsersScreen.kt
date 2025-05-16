package com.example.jonathan.testmvi.features.users.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
 * Displays a form for adding users and a list of created users.
 */
@Composable
fun UsersScreen(viewModel: UsersViewModel = viewModel()) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleAwareFlow = remember(viewModel, lifecycleOwner) {
        viewModel.userState.flowWithLifecycle(
            lifecycle = lifecycleOwner.lifecycle,
            minActiveState = Lifecycle.State.STARTED
        )
    }
    val state by lifecycleAwareFlow.collectAsState(initial = UsersState())
    val scope = rememberCoroutineScope()

    val onNameChange: (String) -> Unit = remember(viewModel, scope) {
        { name -> scope.launch { viewModel.handleIntent(UsersIntent.UpdateName(name)) } }
    }

    val onAgeChange: (String) -> Unit = remember(viewModel, scope) {
        { age -> scope.launch { viewModel.handleIntent(UsersIntent.UpdateAge(age)) } }
    }

    val onLoadUser: () -> Unit = remember(viewModel, scope) {
        { scope.launch { viewModel.handleIntent(UsersIntent.LoadUser) } }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        if (state.isLoading) {
            item {
                CircularProgressIndicator()
            }
        } else {
            item {
                TextField(
                    value = state.name,
                    onValueChange = onNameChange,
                    label = { Text("Name") }
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                TextField(
                    value = state.age,
                    onValueChange = onAgeChange,
                    label = { Text("Age") }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Button(onClick = onLoadUser) {
                    Text("Load User")
                }
            }
        }

        state.error?.let {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Error: $it", color = MaterialTheme.colorScheme.error)
            }
        }

        // Display list of users
        if (state.users.isNotEmpty()) {
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { Text("Created Users:", style = MaterialTheme.typography.titleMedium) }
            item { Spacer(modifier = Modifier.height(8.dp)) }

            items(state.users) { user ->
                Text("- ${user.name}, age ${user.age}")
            }
        }
    }
}
