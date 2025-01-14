package com.example.jonathan.testmvi.intent

sealed class UserIntent {
    object LoadUser : UserIntent()
    data class UpdateName(val name: String) : UserIntent()
    data class UpdateEmail(val email: String) : UserIntent()
}
