package com.example.store.repositories

import com.example.store.models.Credentials
import com.example.store.services.AuthService
import com.example.store.models.LoginPayload
import com.example.store.models.LoginResponse

object AuthRepository {
    private val service = RetrofitBuilder.buildService(AuthService::class.java) as AuthService

    suspend fun login(token: String): LoginResponse {
        return service.login(LoginPayload(token))
    }

    suspend fun localLogin(email: String, password: String): LoginResponse {
        return service.localLogin(Credentials(email, password))
    }

    suspend fun testLogin(token: String) {
        return service.testAuthToken("Bearer $token")
    }
}