package com.example.jonathan.testmvi.model

/**
 * Represents the state of the user screen: User + State:
 */
data class UserState(
    // User data:
    val name: String = "",
    val age: Int = 0,
    // State data:
    val isLoading: Boolean = false,
    val error: String? = null
)
