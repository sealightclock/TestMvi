package com.example.jonathan.testmvi.features.users.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jonathan.testmvi.features.users.domain.entity.UserEntity
import com.example.jonathan.testmvi.features.users.domain.usecase.GetUsersFromLocalUseCase
import com.example.jonathan.testmvi.features.users.domain.usecase.StoreUsersToLocalUseCase
import com.example.jonathan.testmvi.features.users.presentation.intent.UsersIntent
import com.example.jonathan.testmvi.features.users.presentation.state.UsersState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for managing the users screen.
 * Handles user intents and updates UI state accordingly.
 * Persists data using Preferences DataStore via UseCases.
 */
class UsersViewModel(
    private val getUsersFromLocal: GetUsersFromLocalUseCase,
    private val storeUsersToLocal: StoreUsersToLocalUseCase
) : ViewModel() {

    private val _usersState = MutableStateFlow(UsersState())
    val usersState: StateFlow<UsersState> = _usersState.asStateFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent

    init {
        handleIntent(UsersIntent.LoadUsers)
    }

    fun handleIntent(intent: UsersIntent) {
        when (intent) {
            is UsersIntent.UpdateName -> {
                _usersState.update { it.copy(name = intent.newName) }
            }

            is UsersIntent.UpdateAge -> {
                _usersState.update { it.copy(age = intent.newAge) }
            }

            is UsersIntent.AddUser -> {
                viewModelScope.launch {
                    val currentState = _usersState.value
                    val name = currentState.name.trim()
                    val ageStr = currentState.age.trim()

                    // Validate inputs
                    val ageInt = ageStr.toIntOrNull()
                    when {
                        name.isEmpty() -> _errorEvent.emit("Name cannot be empty.")
                        ageInt == null || ageInt !in 0..200 -> _errorEvent.emit("Age must be an integer between 0 and 200.")
                        currentState.users.any { it.name == name && it.age == ageStr } ->
                            _errorEvent.emit("This user already exists.")
                        currentState.users.size >= 5 ->
                            _errorEvent.emit("Cannot add more than 5 users.")
                        else -> {
                            val newUser = UserEntity(name = name, age = ageStr)
                            val updatedList = currentState.users + newUser

                            // Update state
                            _usersState.value = currentState.copy(
                                name = "",
                                age = "0",
                                users = updatedList
                            )

                            // Persist updated list
                            storeUsersToLocal(updatedList)
                        }
                    }
                }
            }

            is UsersIntent.LoadUsers -> {
                viewModelScope.launch {
                    _usersState.update { it.copy(isLoading = true) }
                    val loadedUsers = getUsersFromLocal()
                    _usersState.update { it.copy(isLoading = false, users = loadedUsers) }
                }
            }

            is UsersIntent.ClearError -> {
                // No-op for now, errorEvent is auto-cleared after emission
            }
        }
    }
}
