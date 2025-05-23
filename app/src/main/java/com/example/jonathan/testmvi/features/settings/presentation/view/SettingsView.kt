package com.example.jonathan.testmvi.features.settings.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jonathan.testmvi.shared.ui.CommonTopBar

/**
 * Basic Settings screen used in DrawerNavigation.
 * This is a placeholder — you can expand it with real settings later.
 */
@Composable
fun SettingsScreen() {
    Scaffold(
        topBar = { CommonTopBar(title = "Settings") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Settings Screen", style = MaterialTheme.typography.headlineMedium)
            Text(text = "Add your preferences or toggles here.")
        }
    }
}
