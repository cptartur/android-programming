package com.example.store.services

import com.example.store.models.Cart
import retrofit2.Call
import retrofit2.http.*
import com.example.store.models.ProductAndAmount

interface CartService {

    @GET("cart/{id}")
    fun getUserCarts(@Path("id") id: Int): Call<List<Cart>>

    @GET("cart/{user_id}/{id}")
    fun getUserCartByID(@Path("user_id") userID: Int, @Path("id") id: Int): Call<Cart>

    @POST("cart/{id}")
    fun createCart(@Path("id") userID: Int): Call<Cart>

    @PUT("cart/{id}")
    fun updateCart(@Path("id") cartID: Int, @Body products: List<ProductAndAmount>): Call<Cart>

    @DELETE("cart/{id}")
    fun deleteCart(@Path("id") cartID: Int): Call<Cart>

    @DELETE("cart/{cart_id}/{id}")
    fun deleteProduct(@Path("cart_id") cartID: Int, @Path("id") productID: Int): Call<Cart>
}