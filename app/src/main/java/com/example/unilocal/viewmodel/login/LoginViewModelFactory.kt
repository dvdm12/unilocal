package com.example.unilocal.viewmodel.login

import android.app.Application // Import Application class for context
import androidx.lifecycle.ViewModel // Import ViewModel base class
import androidx.lifecycle.ViewModelProvider // Import ViewModelProvider for factory
import com.example.unilocal.R // Import resources for error messages
import com.example.unilocal.viewmodel.data.UserRepository // Import UserRepository for dependency injection

/**
 * Factory for creating instances of [LoginViewModel] with dependency injection.
 *
 * This class allows:
 * - Passing the application context required for AndroidViewModel.
 * - Injecting the global user repository (UserRepository).
 *
 * Example usage in Navigation.kt:
 * ```
 * val loginViewModel: LoginViewModel = viewModel(
 *     factory = LoginViewModelFactory(
 *         application = context.applicationContext as Application,
 *         userRepository = UserRepository
 *     )
 * )
 * ```
 */
class LoginViewModelFactory( // Factory class for LoginViewModel
    private val application: Application, // Application context for ViewModel
    private val userRepository: UserRepository // UserRepository dependency
) : ViewModelProvider.Factory { // Inherit from ViewModelProvider.Factory

    override fun <T : ViewModel> create(modelClass: Class<T>): T { // Create ViewModel instance
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) { // Check if requested ViewModel is LoginViewModel
            @Suppress("UNCHECKED_CAST") // Suppress unchecked cast warning
            return LoginViewModel(application, userRepository) as T // Return LoginViewModel instance
        }
        throw IllegalArgumentException( // Throw exception for unknown ViewModel
            application.getString(R.string.error_unknown_viewmodel, modelClass.name) // Error message from resources
        )
    }
}
