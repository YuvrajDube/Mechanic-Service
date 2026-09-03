package com.example.mechanicservice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mechanicservice.data.SessionManager
import com.example.mechanicservice.data.model.Service
import com.example.mechanicservice.data.model.ServiceRequest
import com.example.mechanicservice.data.repository.MechanicRepository
import com.example.mechanicservice.data.repository.ServiceRequestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RequestServiceUiState(
    val isLoadingServices: Boolean = false,
    val isSubmitting: Boolean = false,
    val services: List<Service> = emptyList(),
    val isSubmitted: Boolean = false,
    val errorMessage: String? = null
)

class RequestServiceViewModel(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val mechanicRepository = MechanicRepository()
    private val serviceRequestRepository = ServiceRequestRepository(sessionManager)

    private val _uiState = MutableStateFlow(RequestServiceUiState())
    val uiState: StateFlow<RequestServiceUiState> = _uiState.asStateFlow()

    fun loadServices(mechanicId: String) {

        if (mechanicId.isBlank()) {
            _uiState.value = RequestServiceUiState(
                errorMessage = "Invalid mechanic"
            )
            return
        }

        viewModelScope.launch {

            _uiState.value = RequestServiceUiState(
                isLoadingServices = true
            )

            mechanicRepository.getMechanicServices(mechanicId)
                .onSuccess { services ->
                    _uiState.value = RequestServiceUiState(
                        services = services
                    )
                }
                .onFailure { error ->
                    _uiState.value = RequestServiceUiState(
                        errorMessage = error.message
                            ?: "Unable to load services"
                    )
                }
        }
    }

    fun submitRequest(
        mechanicId: String,
        serviceId: String,
        customerName: String,
        phoneNumber: String,
        vehicleNumber: String,
        problemDescription: String
    ) {

        if (customerName.isBlank() ||
            phoneNumber.isBlank() ||
            vehicleNumber.isBlank() ||
            serviceId.isBlank()
        ) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Please fill all required fields"
            )
            return
        }

        val userId = sessionManager.getUserId()

        if (userId.isNullOrBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Session expired. Please login again."
            )
            return
        }

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isSubmitting = true,
                errorMessage = null
            )

            val request = ServiceRequest(
                user_id = userId,
                mechanic_id = mechanicId,
                service_id = serviceId,
                customer_name = customerName.trim(),
                phone_number = phoneNumber.trim(),
                vehicle_number = vehicleNumber.trim(),
                problem_description = problemDescription
                    .trim()
                    .ifBlank { null }
            )

            serviceRequestRepository
                .createRequest(request)
                .onSuccess {

                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        isSubmitted = true
                    )
                }
                .onFailure { error ->

                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        errorMessage = error.message
                            ?: "Unable to submit request"
                    )
                }
        }
    }

}