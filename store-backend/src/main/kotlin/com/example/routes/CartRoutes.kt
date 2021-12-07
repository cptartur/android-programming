package com.example.routes

import com.example.repositories.CartProductRepository
import com.example.repositories.CartRepository
import com.example.repositories.ProductRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureCartRoutes() {
    val baseString = "cart"
    val cartRepository = CartRepository
    val cartProductRepository = CartProductRepository
    routing {
        get("/$baseString/{user_id}") {
            val id = call.parameters["user_id"]?.toInt()
            if (id != null) {
                cartRepository.findAllById(id).let { call.respond(it) }
            }
        }

        get("/$baseString/{user_id}/{cart_id}") {
            val id = call.parameters["cart_id"]?.toInt()
            id?.let { it -> cartProductRepository.findById(it) }?.let { it -> call.respond(it) }
        }

        post("/$baseString/{user_id}") {
            val userID = call.parameters["user_id"]?.toInt()
            if (userID != null) {
                val id = cartRepository.create(userId = userID)
                call.response.apply {
                    status(HttpStatusCode.Created)
                    headers.append(HttpHeaders.Location, id.toString())
                }
            }
        }

        put("/$baseString/{cart_id}") {
            val userID = call.parameters["user_id"]?.toInt()
            val cartID = call.parameters["cart_id"]?.toInt()
            val products = Gson().fromJson<List<ProductAndAmount>>(call.receive())
            products.map {
                val product = ProductRepository.findById(it.id)
                if (product != null && cartID != null) {
                    CartProductRepository.createOrUpdate(cartID, product, it.amount)
                    call.response.status(HttpStatusCode.OK)
                }
            }
        }

        delete("/$baseString/{cart_id}") {
            val cartID = call.parameters["cart_id"]?.toInt()
            if (cartID != null) {
                cartRepository.remove(cartID)
                cartProductRepository.removeAll(cartID)
            }
        }

        delete("/$baseString/{cart_id}/{product_id}") {
            val cartID = call.parameters["cart_id"]?.toInt()
            val productID = call.parameters["product_id"]?.toInt()
            if (cartID != null && productID != null) {
                if (cartProductRepository.remove(cartID, productID)) {
                    call.response.status(HttpStatusCode.OK)
                }
            }
        }
    }
}

private data class ProductAndAmount(val id: Int, val amount: Int) {}
inline fun <reified T> Gson.fromJson(json: String): T = fromJson(json, object: TypeToken<T>() {}.type)
