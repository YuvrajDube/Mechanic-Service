package com.example.mechanicservice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mechanicservice.data.model.Mechanic
import com.example.mechanicservice.data.model.Service
import com.example.mechanicservice.data.repository.MechanicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MechanicDetailsUiState(
    val isLoading: Boolean = false,
    val mechanic: Mechanic? = null,
    val services: List<Service> = emptyList(),
    val errorMessage: String? = null
)

class MechanicDetailsViewModel : ViewModel() {

    private val repository = MechanicRepository()

    private val _uiState =
        MutableStateFlow(MechanicDetailsUiState())

    val uiState: StateFlow<MechanicDetailsUiState> =
        _uiState.asStateFlow()

    fun loadMechanic(mechanicId: String) {

        if (mechanicId.isBlank()) {
            _uiState.value = MechanicDetailsUiState(
                errorMessage = "Invalid mechanic"
            )
            return
        }

        viewModelScope.launch {

            _uiState.value = MechanicDetailsUiState(
                isLoading = true
            )

            val mechanicResult =
                repository.getMechanic(mechanicId)

            val servicesResult =
                repository.getMechanicServices(mechanicId)

            if (mechanicResult.isSuccess) {

                _uiState.value = MechanicDetailsUiState(
                    mechanic = mechanicResult.getOrNull(),
                    services = servicesResult.getOrDefault(emptyList()),
                    errorMessage = servicesResult
                        .exceptionOrNull()
                        ?.message
                )

            } else {

                _uiState.value = MechanicDetailsUiState(
                    errorMessage =
                        mechanicResult.exceptionOrNull()?.message
                            ?: "Unable to load mechanic"
                )
            }
        }
    }
}