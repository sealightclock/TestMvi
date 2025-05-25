package com.example.jonathan.testmvi

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jonathan.testmvi.features.location.presentation.view.LocationScreen
import com.example.jonathan.testmvi.features.settings.presentation.view.SettingsScreen
import com.example.jonathan.testmvi.features.users.presentation.view.UsersScreen
import com.example.jonathan.testmvi.navigation.DrawerDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var currentScreen: DrawerDestination by remember {
        mutableStateOf(DrawerDestination.Users)
    }

    val drawerItems = listOf(
        DrawerDestination.Users,
        DrawerDestination.Settings,
        DrawerDestination.Location
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(12.dp))
                drawerItems.forEach { destination ->
                    NavigationDrawerItem(
                        label = { Text(destination.label) },
                        selected = destination == currentScreen,
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
                                delay(250) // Let drawer finish animation before switching
                                currentScreen = destination
                            }
                        }
                    )
                }
            }
        }
    ) {
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
                when (currentScreen) {
                    is DrawerDestination.Users -> UsersScreen()
                    is DrawerDestination.Settings -> SettingsScreen()
                    is DrawerDestination.Location -> LocationScreen()
                }
            }
        }
    }
}
