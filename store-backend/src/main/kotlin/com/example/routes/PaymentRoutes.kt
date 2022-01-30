package com.example.routes

import com.example.Secrets
import com.google.gson.Gson
import com.stripe.Stripe
import com.stripe.model.PaymentIntent
import com.stripe.model.Product
import com.stripe.param.PaymentIntentCreateParams
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configurePaymentRoutes() {
    Stripe.apiKey = Secrets.STRIPE_API_KEY
    val baseString = "payment"

    routing {
        authenticate("auth-jwt") {
            post("$baseString/create-payment-intent") {
                val total = call.receive<PaymentTotal>()
                println(total.total)
                val params = PaymentIntentCreateParams.builder()
                    .setAmount(total.total)
                    .setCurrency("usd")
                    .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                            .setEnabled(true)
                            .build()
                    )
                    .build()

                val paymentIntent = PaymentIntent.create(params)
                call.respond(hashMapOf("clientSecret" to paymentIntent.clientSecret))
            }
        }
    }
}

data class PaymentTotal(val total: Long)