package com.example.mechanicservice.data.repository

import com.example.mechanicservice.data.model.Mechanic
import com.example.mechanicservice.data.model.Service
import com.example.mechanicservice.data.remote.SupabaseClient
import kotlin.collections.map

class MechanicRepository {

    private val api = SupabaseClient.api

    suspend fun getMechanics(): Result<List<Mechanic>> {
        return try {
            Result.success(api.getMechanics())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMechanic(
        mechanicId: String
    ): Result<Mechanic> {
        return try {

            val mechanics = api.getMechanic(
                id = "eq.$mechanicId"
            )

            val mechanic = mechanics.firstOrNull()
                ?: return Result.failure(
                    Exception("Mechanic not found")
                )

            Result.success(mechanic)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMechanicServices(
        mechanicId: String
    ): Result<List<Service>> {

        return try {

            println("MECHANIC_DEBUG: mechanicId = $mechanicId")

            val response = api.getMechanicServices(
                mechanicId = "eq.$mechanicId"
            )

            println("MECHANIC_DEBUG: response size = ${response.size}")
            println("MECHANIC_DEBUG: response = $response")

            val services = response.map { mechanicService ->
                mechanicService.services
            }

            println("MECHANIC_DEBUG: services = $services")

            Result.success(services)

        } catch (e: Exception) {

            println("MECHANIC_DEBUG: ERROR = ${e.message}")
            e.printStackTrace()

            Result.failure(e)
        }
    }
}