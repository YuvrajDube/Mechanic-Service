package com.example.mechanicservice.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mechanicservice.data.model.Mechanic
import com.example.mechanicservice.data.repository.MechanicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val mechanics: List<Mechanic> = emptyList(),
    val errorMessage: String? = null
)

class HomeViewModel : ViewModel() {

    private val repository = MechanicRepository()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        Log.d("HomeViewModel", "HomeViewModel created")
        loadMechanics()
    }

    fun loadMechanics() {

        Log.d("HomeViewModel", "Loading mechanics...")

        viewModelScope.launch {

            _uiState.value = HomeUiState(
                isLoading = true
            )

            repository.getMechanics()
                .onSuccess { mechanics ->

                    Log.d(
                        "HomeViewModel",
                        "API success. Mechanics count: ${mechanics.size}"
                    )

                    mechanics.forEach { mechanic ->
                        Log.d(
                            "HomeViewModel",
                            "Mechanic: ${mechanic}"
                        )
                    }

                    _uiState.value = HomeUiState(
                        mechanics = mechanics
                    )
                }
                .onFailure { error ->

                    Log.e(
                        "HomeViewModel",
                        "API failed: ${error.message}",
                        error
                    )

                    _uiState.value = HomeUiState(
                        errorMessage =
                            error.message ?: "Unable to load mechanics"
                    )
                }
        }
    }
}