package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import com.example.repositories.CategoryRepository
import com.example.repositories.ProductRepository
import com.example.repositories.UserRepository
import com.example.routes.configureCRUDRoutes
import com.example.routes.configureCartRoutes
import com.example.tables.Carts
import com.example.tables.CartsProducts
import com.example.tables.Products
import com.example.tables.Users
import io.ktor.application.*
import io.ktor.features.*
import org.jetbrains.exposed.sql.*
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

//        Users.insert {
//            it[name] = "Artur"
//        }
    }
    TransactionManager.manager.defaultIsolationLevel =
        Connection.TRANSACTION_SERIALIZABLE
    embeddedServer(Netty, port = 8080, host = "localhost") {
//        configureUserRoutes()
        configureSerialization()
        configureCRUDRoutes("user", UserRepository)
        configureCRUDRoutes("product", ProductRepository)
        configureCRUDRoutes("category", CategoryRepository)
        configureCartRoutes()
//        configureProductRoutes()
        configureRouting()
        install(StatusPages) {

        }
    }.start(wait = true)
}
