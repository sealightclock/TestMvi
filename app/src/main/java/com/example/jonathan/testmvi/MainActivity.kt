package com.example.jonathan.testmvi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.jonathan.testmvi.features.users.presentation.view.UsersScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Connect to the View (Jetpack Compose Screen):
            UsersScreen()
        }
    }
}
