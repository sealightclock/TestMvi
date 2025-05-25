package com.example.jonathan.testmvi

import androidx.compose.animation.Crossfade
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jonathan.testmvi.features.location.presentation.view.LocationScreen
import com.example.jonathan.testmvi.features.settings.presentation.view.SettingsScreen
import com.example.jonathan.testmvi.features.users.presentation.view.UsersScreen
import com.example.jonathan.testmvi.navigation.DrawerDestination
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var currentScreen: DrawerDestination by remember {
        mutableStateOf(DrawerDestination.Users)
    }

    // 🔐 Hardcoded list avoids referencing potentially corrupt DrawerDestination.all
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
                            scope.launch { drawerState.close() }
                            currentScreen = destination
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
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
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
                Crossfade(targetState = currentScreen) { screen ->
                    when (screen) {
                        is DrawerDestination.Users -> UsersScreen()
                        is DrawerDestination.Settings -> SettingsScreen()
                        is DrawerDestination.Location -> LocationScreen()
                    }
                }
            }
        }
    }
}
