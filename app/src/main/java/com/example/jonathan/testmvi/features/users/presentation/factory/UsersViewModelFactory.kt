package com.example.jonathan.testmvi.features.users.presentation.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jonathan.testmvi.features.users.data.datasource.UsersDataStoreApi
import com.example.jonathan.testmvi.features.users.data.datasource.UsersLocalDataSource
import com.example.jonathan.testmvi.features.users.data.repository.UsersRepository
import com.example.jonathan.testmvi.features.users.domain.usecase.GetUsersFromLocal
import com.example.jonathan.testmvi.features.users.domain.usecase.StoreUsersToLocal
import com.example.jonathan.testmvi.features.users.presentation.viewmodel.UsersViewModel

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
        val local = UsersLocalDataSource(api)
        val repo = UsersRepository(local)
        val getUseCase = GetUsersFromLocal(repo)
        val storeUseCase = StoreUsersToLocal(repo)

        return UsersViewModel(getUseCase, storeUseCase) as T
    }
}
