package com.example.jonathan.testmvi.features.users.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jonathan.testmvi.features.users.data.datasource.local.UsersDataStoreApi
import com.example.jonathan.testmvi.features.users.data.datasource.local.UsersDataStoreDataSource
import com.example.jonathan.testmvi.features.users.data.repository.UsersRepository
import com.example.jonathan.testmvi.features.users.domain.usecase.GetUsersFromLocalUseCase
import com.example.jonathan.testmvi.features.users.domain.usecase.StoreUsersToLocalUseCase

/**
 * Factory that creates UsersViewModel with dependencies wired manually.
 */
class UsersViewModelFactory(
    private val appContext: Context // capture application context here
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Initialize everything here using application context
        val api = UsersDataStoreApi(appContext)
        val local = UsersDataStoreDataSource(api)
        val repo = UsersRepository(local)
        val getUseCase = GetUsersFromLocalUseCase(repo)
        val storeUseCase = StoreUsersToLocalUseCase(repo)

        return UsersViewModel(getUseCase, storeUseCase) as T
    }
}