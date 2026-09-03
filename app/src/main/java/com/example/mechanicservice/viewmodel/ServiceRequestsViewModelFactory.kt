package com.example.mechanicservice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mechanicservice.data.SessionManager

class ServiceRequestsViewModelFactory(
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(ServiceRequestsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ServiceRequestsViewModel(sessionManager) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}