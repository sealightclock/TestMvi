package com.example.jonathan.testmvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jonathan.testmvi.intent.UserIntent
import com.example.jonathan.testmvi.model.UserState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state.asStateFlow()

    fun handleIntent(intent: UserIntent) {
        viewModelScope.launch {
            when (intent) {
                is UserIntent.LoadUser -> {
                    _state.value = _state.value.copy(isLoading = true)
                    // Simulate data loading
                    kotlinx.coroutines.delay(1000)
                    _state.value = _state.value.copy(
                        name = "John Doe",
                        email = "john.doe@example.com",
                        isLoading = false
                    )
                }
                is UserIntent.UpdateName -> {
                    _state.value = _state.value.copy(name = intent.name)
                }
                is UserIntent.UpdateEmail -> {
                    _state.value = _state.value.copy(email = intent.email)
                }
            }
        }
    }
}
