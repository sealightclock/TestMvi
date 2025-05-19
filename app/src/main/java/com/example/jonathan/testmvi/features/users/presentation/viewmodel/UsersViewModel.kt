package com.example.jonathan.testmvi.features.users.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jonathan.testmvi.features.users.domain.entity.UserEntity
import com.example.jonathan.testmvi.features.users.presentation.intent.UsersIntent
import com.example.jonathan.testmvi.features.users.presentation.state.UsersState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing the users screen.
 * Handles user intents and updates UI state accordingly.
 */
class UsersViewModel : ViewModel() {

    private val _userState = MutableStateFlow(UsersState())
    val userState: StateFlow<UsersState> = _userState.asStateFlow()

    fun handleIntent(intent: UsersIntent) {
        viewModelScope.launch {
            when (intent) {
                is UsersIntent.LoadUser -> {
                    _userState.value = _userState.value.copy(isLoading = true)

                    try {
                        delay(1000)

                        val currentState = _userState.value
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

                        _userState.value = currentState.copy(
                            users = updatedList,
                            isLoading = false,
                            error = null
                        )
                    } catch (e: Exception) {
                        _userState.value = _userState.value.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }

                is UsersIntent.UpdateName -> {
                    _userState.value = _userState.value.copy(name = intent.name)
                }

                is UsersIntent.UpdateAge -> {
                    _userState.value = _userState.value.copy(age = intent.age)
                }

                is UsersIntent.ClearError -> {
                    _userState.value = _userState.value.copy(error = null)
                }
            }
        }
    }
}
