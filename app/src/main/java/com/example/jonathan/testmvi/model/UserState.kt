package com.example.jonathan.testmvi.model

data class UserState(
    val name: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
