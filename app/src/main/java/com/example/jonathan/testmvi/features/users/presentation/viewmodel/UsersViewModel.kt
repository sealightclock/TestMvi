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

                        val currentAge = _userState.value.age.toIntOrNull() ?: 0
                        if (currentAge >= 5) throw Exception("Too many users loaded.")

                        val nextAge = currentAge + 1
                        val nextName = BASE_USER_NAME + nextAge

                        // Create new UserEntity and append to users list
                        val newUser = UserEntity(name = nextName, age = nextAge.toString())
                        val updatedList = _userState.value.users + newUser

                        _userState.value = _userState.value.copy(
                            name = nextName,
                            age = nextAge.toString(),
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
            }
        }
    }

    companion object {
        private const val BASE_USER_NAME = "User Name"
    }
}
