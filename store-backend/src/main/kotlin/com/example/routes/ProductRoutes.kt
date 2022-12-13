package com.example.routes

import com.example.repositories.ProductRepository
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureProductRoutes() {
    val baseString = "product"
    val repository = ProductRepository

    configureCRUDRoutes(baseString, repository);

    routing {
        get("/$baseString/search") {
            val name = call.parameters["name"]
            val category = call.parameters["category"]

            val products = repository.findByNameAndCategory(name, category)

            call.respond(products)
        }
    }
}