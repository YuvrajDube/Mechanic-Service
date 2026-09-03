package com.example.mechanicservice.data

import android.content.Context
import androidx.core.content.edit

class SessionManager(context: Context) {

    private val preferences = context.getSharedPreferences(
        "mechanic_service_session",
        Context.MODE_PRIVATE
    )

    fun saveSession(
        accessToken: String,
        refreshToken: String?,
        userId: String
    ) {
        preferences.edit {
            putString("access_token", accessToken)
                .putString("refresh_token", refreshToken)
                .putString("user_id", userId)
        }
    }

    fun getAccessToken(): String? {
        return preferences.getString("access_token", null)
    }

    fun getRefreshToken(): String? {
        return preferences.getString("refresh_token", null)
    }

    fun getUserId(): String? {
        return preferences.getString("user_id", null)
    }

    fun isLoggedIn(): Boolean {
        return !getAccessToken().isNullOrBlank()
    }

    fun clearSession() {
        preferences.edit {
            clear()
        }
        }
    }
