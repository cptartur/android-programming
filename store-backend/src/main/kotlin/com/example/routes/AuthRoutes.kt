package com.example.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.auth.GoogleAuth
import com.example.models.User
import com.example.models.UserType
import com.example.repositories.UserRepository
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.util.*

fun Application.configureAuthRoutes() {
    val baseString = "auth"
    val secret = "secret"
    val issuer = "http://0.0.0.0:8080/"

    routing {
        post("$baseString/login") {
            val idToken = call.receive(Token::class)
            val user = GoogleAuth.verifyToken(idToken.idToken)
            if (user != null) {

                val mail = user.email
                val name: String = (user["name"] ?: "NoNameProvided") as String
                if (UserRepository.findByEmail(mail) == null) {
                    UserRepository.create(User(name, 0, mail, null, UserType.GOOGLE))
                }

                val token = JWT.create()
                    .withIssuer(issuer)
                    .withClaim("email", user.email)
                    .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                    .sign(Algorithm.HMAC256(secret))
                call.response.status(HttpStatusCode.OK)
                call.respond(hashMapOf("token" to token))
            } else {
                call.response.status(HttpStatusCode.BadRequest)
            }
        }

        post("$baseString/local_login") {
            val credentials = call.receive<Credentials>()
            val user = UserRepository.findByEmail(credentials.email)
            if (user?.validatePassword(credentials.password) == true) {
                val token = JWT.create()
                    .withIssuer(issuer)
                    .withClaim("email", user.email)
                    .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                    .sign(Algorithm.HMAC256(secret))
                call.response.status(HttpStatusCode.OK)
                call.respond(hashMapOf("token" to token))
            } else {
                call.response.status(HttpStatusCode.BadRequest)
            }
        }

        authenticate("auth-jwt") {
            get("$baseString/protected_route") {
                call.respond(hashMapOf("verified" to true))
            }
        }
    }
}

data class Token(val idToken: String)
data class Credentials(val email: String, val password: String)