package com.example.store.services

import com.example.store.models.LoginPayload
import com.example.store.models.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @POST("auth/login")
    suspend fun login(@Body payload: LoginPayload): LoginResponse

    @GET("auth/protected_route")
    suspend fun testAuthToken(@Header("Authorization") authToken: String)
}

