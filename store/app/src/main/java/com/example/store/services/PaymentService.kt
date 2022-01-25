package com.example.store.services

import com.example.store.models.PaymentIntent
import com.example.store.models.PaymentTotal
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PaymentService {

    @POST("payment/create-payment-intent")
    suspend fun createPaymentIntent(@Header("Authorization") authToken: String, @Body total: PaymentTotal): PaymentIntent

}