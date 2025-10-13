package com.example.unilocal.viewmodel.user.register

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.unilocal.R
import com.example.unilocal.viewmodel.data.UserRepository

/**
 * Factory para crear instancias de RegisterViewModel con dependencias inyectadas.
 */
class RegisterViewModelFactory(
    private val application: Application,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(application, userRepository) as T
        }
        throw IllegalArgumentException(
            application.getString(R.string.error_unknown_viewmodel, modelClass.name)
        )
    }
}
