package com.example.jonathan.testmvi.intent

/**
 * Represents the user intent:
 */
sealed class UserIntent {
    data object LoadUser : UserIntent()
    data class UpdateName(val name: String) : UserIntent()
    data class UpdateAge(val age: Int) : UserIntent()
}
