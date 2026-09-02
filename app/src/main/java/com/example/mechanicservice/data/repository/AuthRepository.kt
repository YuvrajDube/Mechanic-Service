package com.example.mechanicservice.data.repository

import com.example.mechanicservice.data.model.AuthResponse
import com.example.mechanicservice.data.model.LoginRequest
import com.example.mechanicservice.data.model.SignupRequest
import com.example.mechanicservice.data.remote.SupabaseClient

class AuthRepository {

    private val api = SupabaseClient.api

    suspend fun login(
        email: String,
        password: String
    ): Result<AuthResponse> {

        return try {

            val response = api.login(
                request = LoginRequest(
                    email = email,
                    password = password
                )
            )

            Result.success(response)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun signup(
        email: String,
        password: String
    ): Result<AuthResponse> {

        return try {

            val response = api.signup(
                request = SignupRequest(
                    email = email,
                    password = password
                )
            )

            Result.success(response)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}