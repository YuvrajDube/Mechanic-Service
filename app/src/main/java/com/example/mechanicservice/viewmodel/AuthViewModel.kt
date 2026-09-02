package com.example.mechanicservice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mechanicservice.data.model.AuthResponse
import com.example.mechanicservice.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

data class AuthUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val authResponse: AuthResponse? = null
)

class AuthViewModel : ViewModel() {

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
            ).onSuccess { response ->

                _uiState.value = AuthUiState(
                    isSuccess = true,
                    authResponse = response
                )

            }.onFailure { error ->

                _uiState.value = AuthUiState(
                    errorMessage = error.message
                        ?: "Login failed"
                )
                Log.d("failing","Login failed: ${error.message}")
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
            ).onSuccess { response ->

                _uiState.value = AuthUiState(
                    isSuccess = true,
                    authResponse = response
                )

            }.onFailure { error ->

                _uiState.value = AuthUiState(
                    errorMessage = error.message
                        ?: "Signup failed"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }
}