package com.example.jonathan.testmvi.features.user.presentation.intent

/**
 * Represents the user intent:
 */
sealed class UserIntent {
    data object LoadUser : UserIntent()
    data class UpdateName(val name: String) : UserIntent()
    data class UpdateAge(val age: String) : UserIntent()
}
