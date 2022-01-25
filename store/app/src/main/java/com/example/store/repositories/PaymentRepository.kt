package com.example.store.repositories

import com.example.store.models.PaymentIntent
import com.example.store.models.PaymentTotal
import com.example.store.services.PaymentService

object PaymentRepository {
    private val service = RetrofitBuilder.buildService(PaymentService::class.java) as PaymentService

    suspend fun createPaymentIntent(total: PaymentTotal, token: String): PaymentIntent {
        return service.createPaymentIntent("Bearer $token", total)
    }
}
