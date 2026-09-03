package com.example.mechanicservice.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ServiceRequestResponse(
    val id: String,
    val mechanic_id: String,
    val service_id: String,
    val customer_name: String,
    val phone_number: String,
    val vehicle_number: String,
    val problem_description: String? = null,
    val status: String,
    val created_at: String,
    val mechanics: MechanicSummary? = null,
    val services: ServiceSummary? = null
)