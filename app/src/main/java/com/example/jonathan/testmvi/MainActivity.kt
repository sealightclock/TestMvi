package com.example.jonathan.testmvi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.jonathan.testmvi.view.UserScreen
import com.example.jonathan.testmvi.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create ViewModel:
        val viewModel = UserViewModel()

        setContent {
            // Connect to the View (Jetpack Compose Screen):
            UserScreen(viewModel)
        }
    }
}
