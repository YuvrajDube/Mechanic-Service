package com.example.mechanicservice.data.repository

import com.example.mechanicservice.data.SessionManager
import com.example.mechanicservice.data.model.ServiceRequest
import com.example.mechanicservice.data.model.ServiceRequestResponse
import com.example.mechanicservice.data.remote.SupabaseClient

class ServiceRequestRepository(
    private val sessionManager: SessionManager
) {

    private val api = SupabaseClient.api

    suspend fun createRequest(
        request: ServiceRequest
    ): Result<Unit> {

        return try {

            val accessToken = sessionManager.getAccessToken()
                ?: return Result.failure(
                    Exception("Session expired. Please login again.")
                )

            api.createServiceRequest(
                authorization = "Bearer $accessToken",
                request = request
            )

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)

        }
    }

    suspend fun getMyServiceRequests(): Result<List<ServiceRequestResponse>> {

        return try {

            val accessToken = sessionManager.getAccessToken()
                ?: return Result.failure(
                    Exception("Session expired. Please login again.")
                )

            val userId = sessionManager.getUserId()
                ?: return Result.failure(
                    Exception("User not found. Please login again.")
                )

            val response = api.getServiceRequests(
                authorization = "Bearer $accessToken",
                userId = "eq.$userId"
            )

            Result.success(response)

        } catch (e: Exception) {

            Result.failure(e)

        }
    }
}