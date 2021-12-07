package com.example.routes

import com.example.repositories.CartProductRepository
import com.example.repositories.CartRepository
import com.example.repositories.ProductRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureCartRoutes() {
    val baseString = "cart"
    val repository: CartRepository = CartRepository
    val carts: CartProductRepository = CartProductRepository
    routing {
        get("/$baseString/{user_id}") {
            val id = call.parameters["user_id"]?.toInt()
            if (id != null) {
                cartRepository.findAllById(id).let { call.respond(it) }
            }
        }

        get("/$baseString/{user_id}/{cart_id}") {
            val id = call.parameters["cart_id"]?.toInt()
            id?.let { it -> carts.findById(it) }?.let { it -> call.respond(it) }
        }

        post("/$baseString/{user_id}") {
            val userID = call.parameters["user_id"]?.toInt()
            if (userID != null) {
                repository.create(userId = userID)
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
                }
            }
        }

        delete("/$baseString/{cart_id}") {
            val cartID = call.parameters["cart_id"]?.toInt()
            if (cartID != null) {
                repository.remove(cartID)
                carts.removeAll(cartID)
            }
        }

        delete("/$baseString/{cart_id}/{product_id}") {
            val cartID = call.parameters["cart_id"]?.toInt()
            val productID = call.parameters["product_id"]?.toInt()
            if (cartID != null && productID != null) {
                carts.remove(cartID, productID)
            }
        }
    }
}

data class ProductAndAmount(val id: Int, val amount: Int) {}
inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object: TypeToken<T>() {}.type)
