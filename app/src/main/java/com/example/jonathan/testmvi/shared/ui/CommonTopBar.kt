package com.example.jonathan.testmvi.shared.ui

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

/**
 * A reusable top app bar with a centered title.
 * Use this to maintain consistency across screens like Settings and Users.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopBar(title: String) {
    CenterAlignedTopAppBar(
        title = { Text(title) }
    )
}
