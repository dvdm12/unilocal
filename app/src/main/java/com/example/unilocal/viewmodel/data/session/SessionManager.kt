package com.example.unilocal.viewmodel.data.session

import android.content.Context
import android.content.SharedPreferences
import com.example.unilocal.model.User
import kotlinx.serialization.json.Json
import androidx.core.content.edit

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER = "user_data"
    }

    fun saveUser(user: User) {
        val json = Json.encodeToString(user)
        prefs.edit { putString(KEY_USER, json) }
    }

    fun getUser(): User? {
        val json = prefs.getString(KEY_USER, null)
        return json?.let {
            try {
                Json.decodeFromString<User>(it)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun clearUser() {
        prefs.edit { remove(KEY_USER) }
    }
}
