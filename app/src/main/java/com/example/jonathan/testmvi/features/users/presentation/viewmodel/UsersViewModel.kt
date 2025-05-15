package com.example.jonathan.testmvi.features.users.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jonathan.testmvi.features.users.presentation.intent.UsersIntent
import com.example.jonathan.testmvi.features.users.presentation.state.UsersState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for handling user intents and emitting updated UI state.
 * Uses MVI architecture: Intent -> ViewModel -> State -> View.
 */
class UsersViewModel : ViewModel() {

    // Internal mutable state flow; holds the entire UI state
    private val _userState = MutableStateFlow(UsersState())

    // Public read-only state flow exposed to the UI
    val userState: StateFlow<UsersState> = _userState.asStateFlow()

    /**
     * Handles user intent dispatched from the UI.
     */
    fun handleIntent(intent: UsersIntent) {
        viewModelScope.launch {
            when (intent) {
                is UsersIntent.LoadUser -> {
                    // Show loading spinner
                    _userState.value = _userState.value.copy(isLoading = true)

                    try {
                        // Simulate backend delay
                        delay(1000)

                        // Parse current age from state (safely handle non-numeric input)
                        val currentAge = _userState.value.age.toIntOrNull() ?: 0

                        // Simulate failure if too many users are loaded
                        if (currentAge >= 5) throw Exception("Too many users loaded.")

                        // Increment age and generate new name
                        val nextAge = currentAge + 1
                        val nextName = BASE_USER_NAME + nextAge

                        // Atomically update full UI state
                        _userState.value = _userState.value.copy(
                            name = nextName,
                            age = nextAge.toString(),
                            isLoading = false,
                            error = null
                        )
                    } catch (e: Exception) {
                        // Handle simulated failure
                        _userState.value = _userState.value.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }

                is UsersIntent.UpdateName -> {
                    // Update name input from user
                    _userState.value = _userState.value.copy(name = intent.name)
                }

                is UsersIntent.UpdateAge -> {
                    // Update age input from user (as raw string to support safe editing)
                    _userState.value = _userState.value.copy(age = intent.age)
                }
            }
        }
    }

    companion object {
        // Prefix for auto-generated usernames during "Load User"
        private const val BASE_USER_NAME = "User Name"
    }
}
