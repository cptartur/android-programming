package com.example.routes

import com.example.models.Product
import com.example.repositories.ProductRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureProductRoutes() {
    routing {
        get("/product") {
            call.respond(ProductRepository.findAll())
        }

        get("/product/{id}") {
            val id = call.parameters["id"]?.toInt()
            val product = id?.let { it -> ProductRepository.findById(it) }
            product?.let { call.respond(it) }
        }

        post("/product") {
            val product = call.receive(Product::class)
            val tableID = ProductRepository.create(product)
            call.response.apply {
                status(HttpStatusCode.Created)
                headers.append(HttpHeaders.Location, tableID.toString())
            }
        }

        put("/product/{id}") {
            val id = call.parameters["id"]?.toInt()
            val product = call.receive(Product::class)
            product.let {
                if (id != null) {
                    ProductRepository.update(id, product)
                    call.response.status(HttpStatusCode.OK)
                }
            }
        }

        delete("/product/{id}") {
            val id = call.parameters["id"]?.toInt()
            if (id != null) {
                val succeeded = ProductRepository.remove(id)
                if (succeeded) {
                    call.response.status(HttpStatusCode.OK)
                }
            }
        }
    }
}