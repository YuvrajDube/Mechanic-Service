package com.example.mechanicservice.data.remote

import com.example.mechanicservice.data.model.AuthResponse
import com.example.mechanicservice.data.model.LoginRequest
import com.example.mechanicservice.data.model.Mechanic
import com.example.mechanicservice.data.model.Service
import com.example.mechanicservice.data.model.SignupRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SupabaseApi {

    @POST("auth/v1/token")
    suspend fun login(
        @Query("grant_type") grantType: String = "password",
        @Body request: LoginRequest
    ): AuthResponse

    @POST("auth/v1/signup")
    suspend fun signup(
        @Body request: SignupRequest
    ): AuthResponse

    @GET("rest/v1/mechanics")
    suspend fun getMechanics(
        @Query("select") select: String = "*"
    ): List<Mechanic>

    @GET("rest/v1/services")
    suspend fun getServices(
        @Query("select") select: String = "*"
    ): List<Service>
}