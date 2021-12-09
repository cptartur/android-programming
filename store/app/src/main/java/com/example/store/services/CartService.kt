package com.example.store.services

import com.example.store.models.Cart
import retrofit2.Call
import retrofit2.http.*

interface CartService {

    @GET("cart")
    fun getCarts(): Call<List<Cart>>

    @GET("cart/{id}")
    fun getCartByID(@Path("id") id: Int): Call<Cart>

    @POST("cart")
    fun createCart(@Body cart: Cart): Call<Cart>

    @PUT("cart/{id}")
    fun updateCart(@Path("id") id: Int, @Body cart: Cart): Call<Cart>

    @DELETE("cart/{id}")
    fun deleteCart(@Path("id") id: Int): Call<Cart>
}