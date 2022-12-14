package com.example.store.models


data class PaymentIntent(
    var clientSecret: String,
    val ephemeralKey: String,
    val customer: String,
    val publishableKey: String,
)
