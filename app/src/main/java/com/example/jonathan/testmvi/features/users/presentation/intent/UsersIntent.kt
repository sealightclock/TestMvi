package com.example.jonathan.testmvi.features.users.presentation.intent

/**
 * Represents all user intents from the view to the ViewModel.
 */
sealed class UsersIntent {

    // Trigger loading of users from local DataStore
    data object LoadUsers : UsersIntent()

    // Trigger when the name field is edited
    data class UpdateName(val newName: String) : UsersIntent()

    // Trigger when the age field is edited
    data class UpdateAge(val newAge: String) : UsersIntent()

    // Trigger when the user taps "Add User" button
    data object AddUser : UsersIntent()

    // Clear any error messages (e.g., after Snackbar is dismissed)
    data object ClearError : UsersIntent()
}
