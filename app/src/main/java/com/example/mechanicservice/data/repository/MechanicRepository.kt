package com.example.mechanicservice.data.repository

import com.example.mechanicservice.data.model.Mechanic
import com.example.mechanicservice.data.model.Service
import com.example.mechanicservice.data.remote.SupabaseClient

class MechanicRepository {

    private val api = SupabaseClient.api

    suspend fun getMechanics(): Result<List<Mechanic>> {
        return try {
            Result.success(api.getMechanics())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getServices(): Result<List<Service>> {
        return try {
            Result.success(api.getServices())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}