package com.overtaker.app.data.network

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("overtaker_settings", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString("auth_token", null)
    }

    fun clearAuthToken() {
        prefs.edit().remove("auth_token").apply()
    }

    fun saveTheme(isDark: Boolean) {
        prefs.edit().putBoolean("is_dark_theme", isDark).apply()
    }

    fun isDarkTheme(): Boolean {
        // По умолчанию используем темную тему, если не задано обратное
        return prefs.getBoolean("is_dark_theme", true)
    }
}
