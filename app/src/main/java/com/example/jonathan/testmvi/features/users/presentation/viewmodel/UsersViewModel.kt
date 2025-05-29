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
    private val getUsersFromLocalUseCase: GetUsersFromLocalUseCase,
    private val storeUsersToLocalUseCase: StoreUsersToLocalUseCase
) : ViewModel() {

    private val _usersState = MutableStateFlow(UsersState())
    val usersState: StateFlow<UsersState> = _usersState.asStateFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent

    init {
        handleIntent(UsersIntent.LoadCreatedUsers)
    }

    fun handleIntent(intent: UsersIntent) {
        when (intent) {
            is UsersIntent.UpdateInputName -> updateInputName(intent.newName)

            is UsersIntent.UpdateInputAge -> updateInputAge(intent.newAge)

            is UsersIntent.AddUser -> addUser()

            is UsersIntent.LoadCreatedUsers -> loadCreatedUsers()
        }
    }

    private fun updateInputName(newName: String) {
        _usersState.update { it.copy(inputName = newName) }
    }

    private fun updateInputAge(newAge: String) {
        _usersState.update { it.copy(inputAge = newAge) }
    }

    private fun addUser() {
        viewModelScope.launch {
            val currentState = _usersState.value
            val name = currentState.inputName.trim()
            val ageStr = currentState.inputAge.trim()

            // Validate inputs
            val ageInt = ageStr.toIntOrNull()
            when {
                name.isEmpty() -> _errorEvent.emit("Name cannot be empty.")
                ageInt == null || ageInt !in 0..200 -> _errorEvent.emit("Age must be an integer between 0 and 200.")
                currentState.createdUsers.any { it.name == name && it.age == ageStr } ->
                    _errorEvent.emit("This user already exists.")
                currentState.createdUsers.size >= 5 ->
                    _errorEvent.emit("Cannot add more than 5 users.")
                else -> {
                    val newUser = UserEntity(name = name, age = ageStr)
                    val updatedList = currentState.createdUsers + newUser

                    // Update state
                    _usersState.value = currentState.copy(
                        inputName = "",
                        inputAge = "0",
                        createdUsers = updatedList
                    )

                    // Persist updated list
                    storeUsersToLocalUseCase.invoke(updatedList)
                }
            }
        }
    }

    private fun loadCreatedUsers() {
        viewModelScope.launch {
            _usersState.update { it.copy(isLoading = true) }
            val loadedUsers = getUsersFromLocalUseCase.invoke()
            _usersState.update { it.copy(isLoading = false, createdUsers = loadedUsers) }
        }
    }
}
