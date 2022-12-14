package com.example.routes

import com.example.Secrets
import com.stripe.Stripe
import com.stripe.model.Customer
import com.stripe.model.EphemeralKey
import com.stripe.model.PaymentIntent
import com.stripe.param.CustomerCreateParams
import com.stripe.param.EphemeralKeyCreateParams
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

                val customerParams = CustomerCreateParams.builder().build()
                val customer = Customer.create(customerParams)

                val ephemeralKeyParams = EphemeralKeyCreateParams.builder()
                    .setStripeVersion("2022-11-15")
                    .setCustomer(customer.id)
                    .build()
                val ephemeralKey = EphemeralKey.create(ephemeralKeyParams)

                val params = PaymentIntentCreateParams.builder()
                    .setAmount(total.total)
                    .setCurrency("usd")
                    .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                            .setEnabled(true)
                            .build()
                    )
                    .setCustomer(customer.id)
                    .build()
                val paymentIntent = PaymentIntent.create(params)

                call.respond(
                    hashMapOf(
                        "clientSecret" to paymentIntent.clientSecret,
                        "ephemeralKey" to ephemeralKey.secret,
                        "customer" to customer.id,
                        "publishableKey" to Secrets.STRIPE_PUBLIC_KEY,
                    )
                )
            }
        }
    }
}

data class PaymentTotal(val total: Long)