package com.example.mechanicservice.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Mechanic(
    val id: String,
    val garage_name: String,
    val rating: Double,
    val distance_km: Double,
    val location: String,
    val address: String,
    val working_hours: String,
    val phone_number: String,
    val is_open: Boolean,
    val cover_image_url: String? = null
)