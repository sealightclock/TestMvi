package com.example.jonathan.testmvi

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.example.jonathan.testmvi.features.users.presentation.view.UsersScreen
import com.example.jonathan.testmvi.features.settings.presentation.view.SettingsScreen
import com.example.jonathan.testmvi.navigation.DrawerDestination
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(12.dp))
                DrawerDestination.all.forEach { destination ->
                    NavigationDrawerItem(
                        label = { Text(destination.label) },
                        selected = currentRoute == destination.route,
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
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
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = DrawerDestination.Users.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(DrawerDestination.Users.route) { UsersScreen() }
                composable(DrawerDestination.Settings.route) { SettingsScreen() }
            }
        }
    }
}
