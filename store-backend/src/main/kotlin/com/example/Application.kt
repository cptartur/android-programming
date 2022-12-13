package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import com.example.repositories.AddressRepository
import com.example.repositories.CategoryRepository
import com.example.repositories.UserRepository
import com.example.routes.*
import com.example.tables.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

fun main() {
    Database.connect("jdbc:sqlite:database.sqlite", "org.sqlite.JDBC")
//    Database.connect("jdbc:sqlite:file:test?mode=memory&cache=shared", "org.sqlite.JDBC")
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(Users)
        SchemaUtils.create(Products)
        SchemaUtils.create(Carts)
        SchemaUtils.create(CartsProducts)
        SchemaUtils.create(Addresses)
    }
    TransactionManager.manager.defaultIsolationLevel =
        Connection.TRANSACTION_SERIALIZABLE
    embeddedServer(Netty, port = 8080, host = "localhost") {
        configureSerialization()
        install(Authentication) {
            jwt("auth-jwt") {
                verifier(
                    JWT
                        .require(Algorithm.HMAC256("secret"))
                        .withIssuer("http://0.0.0.0:8080/")
                        .build()
                )
                validate { credential ->
                    JWTPrincipal(credential.payload)
                }
            }
        }
        configureCRUDRoutes("user", UserRepository)
        configureCRUDRoutes("category", CategoryRepository)
        configureCRUDRoutes("address", AddressRepository)
        configureCartRoutes()
        configureProductRoutes()
        configureAuthRoutes()
        configurePaymentRoutes()
        configureRouting()
        install(StatusPages) {
        }

    }.start(wait = true)
}
