package com.example.jonathan.testmvi

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.jonathan.testmvi.features.location.presentation.view.LocationScreen
import com.example.jonathan.testmvi.features.settings.presentation.view.SettingsScreen
import com.example.jonathan.testmvi.features.users.presentation.view.UsersScreen
import com.example.jonathan.testmvi.shared.navigation.DrawerDestination
import com.example.jonathan.testmvi.shared.preferences.ScreenDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootScreen() {
    val context = LocalContext.current.applicationContext
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val screenBackStack = remember { mutableStateListOf<DrawerDestination>() }

    // Load last screen from DataStore (nullable at first)
    val lastScreenState = produceState<DrawerDestination?>(initialValue = null, context) {
        ScreenDataStore.lastScreenFlow(context)
            .collect { savedRoute ->
                value = DrawerDestination.fromRoute(savedRoute ?: DrawerDestination.Users.route)
            }
    }

    val isScreenRestored = lastScreenState.value != null
    val currentScreen = remember { derivedStateOf { screenBackStack.lastOrNull() } }

    // Track navigation animation state
    val isNavigating = remember { mutableStateOf(false) }

    // Initialize the backstack with restored screen
    LaunchedEffect(isScreenRestored) {
        if (isScreenRestored && screenBackStack.isEmpty()) {
            screenBackStack.add(lastScreenState.value!!)
        }
    }

    val drawerItems = listOf(
        DrawerDestination.Users,
        DrawerDestination.Settings,
        DrawerDestination.Location
    )

    BackHandler(enabled = screenBackStack.size > 1) {
        screenBackStack.removeAt(screenBackStack.lastIndex)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(12.dp))
                drawerItems.forEach { destination ->
                    NavigationDrawerItem(
                        label = { Text(destination.label) },
                        selected = destination == currentScreen.value,
                        icon = {
                            val icon = when (destination) {
                                is DrawerDestination.Users -> Icons.Default.Person
                                is DrawerDestination.Settings -> Icons.Default.Settings
                                is DrawerDestination.Location -> Icons.Default.LocationOn
                            }
                            Icon(imageVector = icon, contentDescription = destination.label)
                        },
                        onClick = {
                            scope.launch {
                                if (screenBackStack.lastOrNull() != destination) {
                                    isNavigating.value = true
                                    screenBackStack.add(destination)
                                    ScreenDataStore.saveLastScreen(context, destination.route)
                                    drawerState.close()
                                    delay(250) // Delay after screen change
                                    isNavigating.value = false
                                } else {
                                    drawerState.close()
                                }
                            }
                        }
                    )
                }
            }
        }
    ) {
        if (isScreenRestored) {
            if (isNavigating.value) {
                // Show spinner during navigation transition
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("TestMvi App") },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch { drawerState.open() }
                                }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentScreen.value) {
                            is DrawerDestination.Users -> UsersScreen()
                            is DrawerDestination.Settings -> SettingsScreen()
                            is DrawerDestination.Location -> LocationScreen()
                            null -> {} // should never happen after init
                        }
                    }
                }
            }
        } else {
            // Initial loading state while DataStore is read
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
