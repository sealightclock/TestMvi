package com.example.jonathan.testmvi.features.user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jonathan.testmvi.features.user.presentation.intent.UserIntent
import com.example.jonathan.testmvi.features.user.presentation.model.UserState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    fun handleIntent(intent: UserIntent) {
        viewModelScope.launch {
            when (intent) {
                is UserIntent.LoadUser -> {
                    _userState.value = _userState.value.copy(isLoading = true)
                    // Simulate data loading
                    delay(1000)
                    _userState.value = _userState.value.copy(
                        name = BASE_USER_NAME + userAge,
                        age = userAge++,
                        isLoading = false
                    )
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
        private var userAge = 0
    }
}
