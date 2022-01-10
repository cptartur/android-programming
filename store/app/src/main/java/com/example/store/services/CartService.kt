package com.example.store.services

import com.example.store.models.Cart
import retrofit2.Call
import retrofit2.http.*
import com.example.store.models.ProductAndAmount

interface CartService {

    @GET("cart/{id}")
    suspend fun getUserCarts(@Path("id") id: Int): List<Cart>

    @GET("cart/{user_id}/{id}")
    suspend fun getUserCartByID(@Path("user_id") userID: Int, @Path("id") id: Int): Cart

    @POST("cart/{id}")
    suspend fun createCart(@Path("id") userID: Int)

    @PUT("cart/{id}")
    suspend fun updateCart(@Path("id") cartID: Int, @Body products: List<ProductAndAmount>)

    @DELETE("cart/{id}")
    suspend fun deleteCart(@Path("id") cartID: Int)

    @DELETE("cart/{cart_id}/{id}")
    suspend fun deleteProduct(@Path("cart_id") cartID: Int, @Path("id") productID: Int)
}