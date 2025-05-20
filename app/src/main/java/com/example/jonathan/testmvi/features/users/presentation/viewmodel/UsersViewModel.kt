package com.example.jonathan.testmvi.features.users.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jonathan.testmvi.features.users.domain.entity.UserEntity
import com.example.jonathan.testmvi.features.users.presentation.intent.UsersIntent
import com.example.jonathan.testmvi.features.users.presentation.state.UsersState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for managing the users screen.
 * Handles user intents and updates UI state accordingly.
 */
class UsersViewModel : ViewModel() {

    private val _usersState = MutableStateFlow(UsersState())
    val usersState: StateFlow<UsersState> = _usersState.asStateFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent

    fun handleIntent(intent: UsersIntent) {
        viewModelScope.launch {
            when (intent) {
                is UsersIntent.LoadUser -> {
                    _usersState.value = _usersState.value.copy(isLoading = true)

                    try {
                        delay(1000)

                        val currentState = _usersState.value
                        val name = currentState.name.trim()
                        val ageStr = currentState.age.trim()

                        if (name.isEmpty()) throw Exception("Name cannot be empty.")
                        val ageInt = ageStr.toIntOrNull()
                        if (ageInt == null || ageInt !in 0..200) {
                            throw Exception("Age must be an integer between 0 and 200.")
                        }
                        if (currentState.users.any { it.name == name && it.age == ageStr }) {
                            throw Exception("This user already exists.")
                        }
                        if (currentState.users.size >= 5) {
                            throw Exception("Cannot add more than 5 users.")
                        }

                        val newUser = UserEntity(name = name, age = ageStr)
                        val updatedList = currentState.users + newUser

                        _usersState.value = currentState.copy(
                            users = updatedList,
                            isLoading = false
                        )
                    } catch (e: Exception) {
                        _usersState.value = _usersState.value.copy(isLoading = false)
                        _errorEvent.emit(e.message ?: "Unknown error")
                    }
                }

                is UsersIntent.UpdateName -> {
                    _usersState.value = _usersState.value.copy(name = intent.name)
                }

                is UsersIntent.UpdateAge -> {
                    _usersState.value = _usersState.value.copy(age = intent.age)
                }

                is UsersIntent.ClearError -> {
                    // No longer needed
                }
            }
        }
    }
}
