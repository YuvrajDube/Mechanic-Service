package com.example.mechanicservice.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ServiceRequest(
    val user_id: String,
    val mechanic_id: String,
    val service_id: String,
    val customer_name: String,
    val phone_number: String,
    val vehicle_number: String,
    val problem_description: String? = null
)