package com.example.mechanicservice.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MechanicService(
    val mechanic_id: String,
    val service_id: String
)