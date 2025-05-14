package com.example.jonathan.testmvi.features.user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jonathan.testmvi.features.user.presentation.intent.UserIntent
import com.example.jonathan.testmvi.features.user.presentation.state.UserState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private var userAge = 0 // TODO: For ViewModel, it is better to use Int rather than String.

    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    fun handleIntent(intent: UserIntent) {
        viewModelScope.launch {
            when (intent) {
                is UserIntent.LoadUser -> {
                    _userState.value = _userState.value.copy(isLoading = true)
                    // Simulate data loading
                    try {
                        delay(1000)
                        if (userAge >= 5) throw Exception("Too many users loaded.")
                        _userState.value = _userState.value.copy(
                            name = BASE_USER_NAME + userAge,
                            age = (userAge++).toString(),
                            isLoading = false
                        )
                    } catch (e: Exception) {
                        _userState.value = _userState.value.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }
                is UserIntent.UpdateName -> {
                    _userState.value = _userState.value.copy(name = intent.name)
                }
                is UserIntent.UpdateAge -> {
                    _userState.value = _userState.value.copy(age = intent.age)
                }
            }
        }
    }

    companion object {
        private const val BASE_USER_NAME = "User Name"
    }
}
