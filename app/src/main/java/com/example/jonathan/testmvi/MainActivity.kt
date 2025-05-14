package com.example.jonathan.testmvi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.jonathan.testmvi.features.user.presentation.view.UserScreen
import com.example.jonathan.testmvi.features.user.presentation.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Connect to the View (Jetpack Compose Screen):
            UserScreen(viewModel)
        }
    }
}
