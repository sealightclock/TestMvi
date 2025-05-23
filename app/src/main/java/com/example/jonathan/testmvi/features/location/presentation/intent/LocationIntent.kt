package com.example.jonathan.testmvi.features.location.presentation.intent

sealed class LocationIntent {
    object LoadLocation : LocationIntent()
}
