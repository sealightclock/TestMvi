package com.example.jonathan.testmvi.features.user.presentation.state

/**
 * Represents the state of the user screen: User + State:
 */
data class UserState(
    // User data:
    val name: String = "",
    val age: String = "0", // TODO: For State, it is better to use String rather than Int
    // to avoid NumberFormatException in TextField
    // State data:
    val isLoading: Boolean = false,
    val error: String? = null
)
