package com.example.unilocal.viewmodel.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserSessionViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserSessionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserSessionViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}