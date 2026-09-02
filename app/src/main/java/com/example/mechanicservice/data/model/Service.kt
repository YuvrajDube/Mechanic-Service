package com.example.mechanicservice.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Service(
    val id: String,
    val name: String
)