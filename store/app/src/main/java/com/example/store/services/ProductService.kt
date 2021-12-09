package com.example.store.services

import com.example.store.models.Product
import retrofit2.Call
import retrofit2.http.*

interface ProductService {

    @GET("product")
    fun getProducts(): Call<List<Product>>

    @GET("product/{id}")
    fun getProductByID(@Path("id") id: Int): Call<Product>

    @POST("product")
    fun createProduct(@Body product: Product): Call<Product>

    @PUT("product/{id}")
    fun updateProduct(@Path("id") id: Int, @Body product: Product): Call<Product>

    @DELETE("product/{id}")
    fun deleteProduct(@Path("id") id: Int): Call<Product>
}