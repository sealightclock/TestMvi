package com.example.jonathan.testmvi.features.users.presentation.intent

/**
 * Represents the user intent:
 */
sealed class UsersIntent {
    data object LoadUser : UsersIntent()
    data class UpdateName(val name: String) : UsersIntent()
    data class UpdateAge(val age: String) : UsersIntent()

    // Used to clear error after showing Snackbar
    data object ClearError : UsersIntent()
}
