package com.example.store.repositories

import com.example.store.models.Cart
import com.example.store.models.Product
import com.example.store.models.ProductAndAmount
import com.example.store.models.User
import com.example.store.services.CartService
import com.example.store.services.ProductService
import com.example.store.services.UserService
import retrofit2.Callback

object CartRepository {
    private val service = RetrofitBuilder.buildService(CartService::class.java) as CartService

    fun getCarts(id: Int, callback: Callback<List<Cart>>) {
        val call = service.getUserCarts(id)
        call.enqueue(callback)
    }

    fun getCart(userId: Int, id: Int, callback: Callback<Cart?>) {
        val call = service.getUserCartByID(userId, id)
        call.enqueue(callback)
    }

    fun createCart(userId: Int, callback: Callback<Cart>) {
        val call = service.createCart(userId)
        call.enqueue(callback)
    }

    fun updateCart(cartId: Int, products: List<ProductAndAmount>, callback: Callback<Cart>) {
        val call = service.updateCart(cartId, products)
        call.enqueue(callback)
    }

    fun deleteProduct(cartId: Int, id: Int, callback: Callback<Cart>) {
        val call = service.deleteProduct(cartId, id)
        call.enqueue(callback)
    }

    fun deleteCart(cartId: Int, callback: Callback<Cart>) {
        val call = service.deleteCart(cartId)
        call.enqueue(callback)
    }
}