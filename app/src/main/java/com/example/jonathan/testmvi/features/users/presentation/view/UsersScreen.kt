package com.example.jonathan.testmvi.features.users.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
 * - Uses Snackbar for error feedback without affecting layout.
 * - Keeps layout stable when loading (no layout shift or scroll jump).
 * - Disables inputs while loading.
 */
@Composable
fun UsersScreen(viewModel: UsersViewModel = viewModel()) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleAwareFlow = remember(viewModel, lifecycleOwner) {
        viewModel.usersState.flowWithLifecycle(
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

    val snackbarHostState = remember { SnackbarHostState() }

    // Show snackbar when there's a new error, then clear it only after dismissal
    LaunchedEffect(state.error) {
        state.error?.let { message ->
            val result = snackbarHostState.showSnackbar(message)
            if (result == SnackbarResult.Dismissed || result == SnackbarResult.ActionPerformed) {
                viewModel.handleIntent(UsersIntent.ClearError)
            }
        }
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
                        onClick = onLoadUser,
                        enabled = !state.isLoading
                    ) {
                        Text("Load User")
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
}
