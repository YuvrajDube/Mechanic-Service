package com.example.mechanicservice.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MechanicServiceWithService(
    val service_id: String,
    val services: Service
)