package com.example.mechanicservice.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mechanicservice.data.SessionManager
import com.example.mechanicservice.data.model.AuthResponse
import com.example.mechanicservice.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val authResponse: AuthResponse? = null
)

class AuthViewModel(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val repository = AuthRepository()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()


    fun login(
        email: String,
        password: String
    ) {

        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState(
                errorMessage = "Email and password are required"
            )
            return
        }

        viewModelScope.launch {

            _uiState.value = AuthUiState(
                isLoading = true
            )

            repository.login(
                email = email.trim(),
                password = password
            )
                .onSuccess { response ->

                    val accessToken = response.access_token
                    val user = response.user

                    if (accessToken != null && user != null) {

                        // Save session locally
                        sessionManager.saveSession(
                            accessToken = accessToken,
                            refreshToken = response.refresh_token,
                            userId = user.id
                        )

                        Log.d(
                            "AuthViewModel",
                            "Login successful"
                        )

                        Log.d(
                            "AuthViewModel",
                            "Session saved for user: ${user.id}"
                        )

                        _uiState.value = AuthUiState(
                            isSuccess = true,
                            authResponse = response
                        )

                    } else {

                        _uiState.value = AuthUiState(
                            errorMessage = "Invalid login response"
                        )

                        Log.e(
                            "AuthViewModel",
                            "Login response missing access token or user"
                        )
                    }
                }
                .onFailure { error ->

                    Log.e(
                        "AuthViewModel",
                        "Login failed",
                        error
                    )

                    _uiState.value = AuthUiState(
                        errorMessage = error.message
                            ?: "Login failed"
                    )
                }
        }
    }

    fun signup(
        email: String,
        password: String
    ) {

        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState(
                errorMessage = "Email and password are required"
            )
            return
        }

        if (password.length < 6) {
            _uiState.value = AuthUiState(
                errorMessage = "Password must be at least 6 characters"
            )
            return
        }

        viewModelScope.launch {

            _uiState.value = AuthUiState(
                isLoading = true
            )

            repository.signup(
                email = email.trim(),
                password = password
            )
                .onSuccess { response ->

                    val accessToken = response.access_token
                    val user = response.user

                    /*
                     * Supabase may require email confirmation.
                     * In that case accessToken/user can be null.
                     */
                    if (accessToken != null && user != null) {

                        sessionManager.saveSession(
                            accessToken = accessToken,
                            refreshToken = response.refresh_token,
                            userId = user.id
                        )

                        Log.d(
                            "AuthViewModel",
                            "Signup successful and session saved"
                        )
                    }

                    _uiState.value = AuthUiState(
                        isSuccess = true,
                        authResponse = response
                    )
                }
                .onFailure { error ->

                    Log.e(
                        "AuthViewModel",
                        "Signup failed",
                        error
                    )

                    _uiState.value = AuthUiState(
                        errorMessage = error.message
                            ?: "Signup failed"
                    )
                }
        }
    }


}