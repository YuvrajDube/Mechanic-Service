package com.example.mechanicservice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mechanicservice.data.SessionManager
import com.example.mechanicservice.data.model.ServiceRequestResponse
import com.example.mechanicservice.data.repository.ServiceRequestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log


data class ServiceRequestsUiState(
    val isLoading: Boolean = false,
    val requests: List<ServiceRequestResponse> = emptyList(),
    val errorMessage: String? = null
)

class ServiceRequestsViewModel(
    sessionManager: SessionManager
) : ViewModel() {

    private val repository =
        ServiceRequestRepository(sessionManager)

    private val _uiState =
        MutableStateFlow(ServiceRequestsUiState())

    val uiState: StateFlow<ServiceRequestsUiState> =
        _uiState.asStateFlow()

    init {
        loadRequests()
    }

    fun loadRequests() {

        viewModelScope.launch {

            _uiState.value =
                ServiceRequestsUiState(isLoading = true)

            repository.getMyServiceRequests()
                .onSuccess { requests ->

                    _uiState.value =
                        ServiceRequestsUiState(
                            requests = requests
                        )
                }
                .onFailure { error ->

                    Log.d("ServiceRequestsViewModel", "Error loading service history", error)

                    _uiState.value =
                        ServiceRequestsUiState(
                            errorMessage =
                                error.message
                                    ?: "Unable to load service history"
                        )
                }
        }
    }
}