package com.example.mechanicservice.data.remote

import com.example.mechanicservice.data.model.AuthResponse
import com.example.mechanicservice.data.model.LoginRequest
import com.example.mechanicservice.data.model.Mechanic
import com.example.mechanicservice.data.model.MechanicServiceWithService
import com.example.mechanicservice.data.model.Service
import com.example.mechanicservice.data.model.ServiceRequest
import com.example.mechanicservice.data.model.ServiceRequestResponse
import com.example.mechanicservice.data.model.SignupRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
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


    @GET("rest/v1/mechanics")
    suspend fun getMechanic(
        @Query("id") id: String,
        @Query("select") select: String = "*"
    ): List<Mechanic>

    @GET("rest/v1/mechanic_services")
    suspend fun getMechanicServices(
        @Query("mechanic_id") mechanicId: String,
        @Query("select") select: String = "service_id,services(id,name)"
    ): List<MechanicServiceWithService>


    @POST("rest/v1/service_requests")
    suspend fun createServiceRequest(
        @Header("Authorization") authorization: String,
        @Body request: ServiceRequest
    )

    @GET("rest/v1/service_requests")
    suspend fun getServiceRequests(
        @Header("Authorization") authorization: String,
        @Query("user_id") userId: String,
        @Query("select")
        select: String = "*,mechanics(garage_name),services(name)",
        @Query("order")
        order: String = "created_at.desc"
    ): List<ServiceRequestResponse>
}