package com.example.jonathan.testmvi.features.users.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jonathan.testmvi.features.users.presentation.factory.UsersViewModelFactory
import com.example.jonathan.testmvi.features.users.presentation.intent.UsersIntent
import com.example.jonathan.testmvi.features.users.presentation.state.UsersState
import com.example.jonathan.testmvi.features.users.presentation.viewmodel.UsersViewModel

/**
 * Displays a form for adding users and a list of created users.
 * - Uses Snackbar for error feedback without affecting layout.
 * - Keeps layout stable when loading (no layout shift or scroll jump).
 * - Disables inputs while loading.
 */
@Composable
fun UsersScreen() {
    // Provide ViewModel using custom factory
    val context = LocalContext.current.applicationContext
    val factory = remember { UsersViewModelFactory(context) }
    val viewModel: UsersViewModel = viewModel(factory = factory)

    // Lifecycle-aware state collection
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleAwareFlow = remember(viewModel, lifecycleOwner) {
        viewModel.usersState.flowWithLifecycle(
            lifecycle = lifecycleOwner.lifecycle,
            minActiveState = Lifecycle.State.STARTED
        )
    }
    val state by lifecycleAwareFlow.collectAsState(initial = UsersState())

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Show Snackbar on error event
    LaunchedEffect(Unit) {
        viewModel.errorEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Intent dispatchers
    val onNameChange: (String) -> Unit = remember(viewModel) {
        { viewModel.handleIntent(UsersIntent.UpdateName(it)) }
    }

    val onAgeChange: (String) -> Unit = remember(viewModel) {
        { viewModel.handleIntent(UsersIntent.UpdateAge(it)) }
    }

    val onAddUser: () -> Unit = remember(viewModel) {
        { viewModel.handleIntent(UsersIntent.AddUser) }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top
        ) {
            item {
                TextField(
                    value = state.name,
                    onValueChange = onNameChange,
                    label = { Text("Name") },
                    enabled = !state.isLoading
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                TextField(
                    value = state.age,
                    onValueChange = onAgeChange,
                    label = { Text("Age") },
                    enabled = !state.isLoading
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = onAddUser,
                        enabled = !state.isLoading
                    ) {
                        Text("Add User")
                    }

                    if (state.isLoading) {
                        Spacer(modifier = Modifier.width(12.dp))
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }
            }

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
}
