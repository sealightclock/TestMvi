package com.example.jonathan.testmvi

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.jonathan.testmvi.features.location.presentation.view.LocationScreen
import com.example.jonathan.testmvi.features.settings.presentation.view.SettingsScreen
import com.example.jonathan.testmvi.features.users.presentation.view.UsersScreen
import com.example.jonathan.testmvi.navigation.DrawerDestination
import com.example.jonathan.testmvi.shared.preferences.ScreenPreferencesDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current.applicationContext
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val screenBackStack = remember { mutableStateListOf<DrawerDestination>() }

    // Load last screen from DataStore (nullable at first)
    val lastScreenState = produceState<DrawerDestination?>(initialValue = null, context) {
        ScreenPreferencesDataStore.lastScreenFlow(context)
            .collect { savedRoute ->
                value = DrawerDestination.fromRoute(savedRoute ?: DrawerDestination.Users.route)
            }
    }

    val isScreenRestored = lastScreenState.value != null
    val currentScreen = remember { derivedStateOf { screenBackStack.lastOrNull() } }

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
                                drawerState.close()
                                delay(250)
                                if (screenBackStack.lastOrNull() != destination) {
                                    screenBackStack.add(destination)
                                    ScreenPreferencesDataStore.saveLastScreen(
                                        context,
                                        destination.route
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    ) {
        if (isScreenRestored) {
            Scaffold(
                topBar = {
                    TopAppBar(
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
