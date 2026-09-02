package com.example.mechanicservice.data.remote

import com.example.mechanicservice.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object SupabaseClient {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val authInterceptor = Interceptor { chain ->

        val request = chain.request()
            .newBuilder()
            .addHeader(
                "apikey",
                BuildConfig.SUPABASE_KEY
            )
            .addHeader(
                "Content-Type",
                "application/json"
            )
            .build()

        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.SUPABASE_URL)
        .client(okHttpClient)
        .addConverterFactory(
            json.asConverterFactory(
                "application/json".toMediaType()
            )
        )
        .build()

    val api: SupabaseApi =
        retrofit.create(SupabaseApi::class.java)
}