package com.example.jonathan.testmvi.features.location.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jonathan.testmvi.features.location.data.datasource.platform.LocationDataSource
import com.example.jonathan.testmvi.features.location.data.repository.LocationRepositoryImpl
import com.example.jonathan.testmvi.shared.domain.location.usecase.GetCurrentLocationUseCase

/**
 * Factory to create LocationViewModel with dependencies injected.
 *
 * This helps with testability and consistency across screens.
 */
class LocationViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dataSource = LocationDataSource(context)
        val repo = LocationRepositoryImpl(dataSource)
        val useCase = GetCurrentLocationUseCase(repo)
        return LocationViewModel(useCase) as T
    }
}
