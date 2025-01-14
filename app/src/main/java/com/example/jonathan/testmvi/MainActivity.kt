package com.example.jonathan.testmvi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.jonathan.testmvi.view.UserScreen
import com.example.jonathan.testmvi.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel = UserViewModel()

        setContent {
            UserScreen(viewModel)
        }
    }
}

