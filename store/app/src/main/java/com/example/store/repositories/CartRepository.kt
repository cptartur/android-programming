package com.example.store.repositories

import com.example.store.models.Cart
import com.example.store.models.ProductAndAmount
import com.example.store.services.CartService

object CartRepository {
    private val service = RetrofitBuilder.buildService(CartService::class.java) as CartService

    suspend fun getCarts(id: Int): List<Cart> {
       return service.getUserCarts(id)
    }

    suspend fun getCart(userId: Int, id: Int): Cart {
        return service.getUserCartByID(userId, id)
    }

    suspend fun createCart(userId: Int) {
        return service.createCart(userId)
    }

    suspend fun updateCart(cartId: Int, products: List<ProductAndAmount>) {
        return service.updateCart(cartId, products)
    }

    suspend fun deleteProduct(cartId: Int, id: Int) {
        return service.deleteProduct(cartId, id)
    }

    suspend fun deleteCart(cartId: Int) {
        return service.deleteCart(cartId)
    }
}