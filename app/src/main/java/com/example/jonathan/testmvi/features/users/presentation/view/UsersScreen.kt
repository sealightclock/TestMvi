package com.example.jonathan.testmvi.features.users.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.jonathan.testmvi.shared.ui.CommonTopBar
import kotlinx.coroutines.flow.collectLatest

/**
 * Displays a form for adding users and a list of created users.
 * - Uses Snackbar for error feedback without affecting layout.
 * - Keeps layout stable when loading (no layout shift or scroll jump).
 * - Disables inputs while loading.
 * - Ensures Snackbar stays visible above soft keyboard.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen() {
    val context = LocalContext.current.applicationContext
    val factory = remember { UsersViewModelFactory(context) }
    val viewModel: UsersViewModel = viewModel(factory = factory)

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleAwareFlow = remember(viewModel, lifecycleOwner) {
        viewModel.usersState.flowWithLifecycle(
            lifecycle = lifecycleOwner.lifecycle,
            minActiveState = Lifecycle.State.STARTED
        )
    }
    val state by lifecycleAwareFlow.collectAsState(initial = UsersState())

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.errorEvent.collectLatest { message ->
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(message)
        }
    }

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
        topBar = { CommonTopBar(title = "Users") }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
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
                    item {
                        Text(
                            "Created Users:",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    items(state.users) { user ->
                        Text("- ${user.name}, age ${user.age}")
                    }
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            )
        }
    }
}
