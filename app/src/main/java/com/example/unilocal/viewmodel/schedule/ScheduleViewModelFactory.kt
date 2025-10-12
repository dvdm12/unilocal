package com.example.unilocal.viewmodel.schedule

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory class responsible for creating instances of [ScheduleViewModel].
 * It ensures that the ViewModel receives a valid [Application] context,
 * which is required for accessing localized resources and other
 * context-dependent operations.
 *
 * This approach keeps lifecycle awareness and prevents memory leaks.
 */
class ScheduleViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            return ScheduleViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
