package com.example.store.repositories

import com.example.store.services.ProductService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private val baseURL: String = "https://5f25-83-22-151-252.ngrok.io"

    fun buildService(service: Class<*>): Any? {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(service)
    }
}