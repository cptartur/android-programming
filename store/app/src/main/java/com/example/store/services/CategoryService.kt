package com.example.store.services

import com.example.store.models.Category
import retrofit2.Call
import retrofit2.http.*

interface CategoryService {

    @GET("category")
    suspend fun getCategories(): List<Category>

    @GET("category/{id}")
    suspend fun getCategoryByID(@Path("id") id: Int): Category

    @POST("category")
    suspend fun createCategory(@Body category: Category)

    @PUT("category/{id}")
    suspend fun updateCategory(@Path("id") id: Int, @Body category: Category)

    @DELETE("category/{id}")
    suspend fun deleteCategory(@Path("id") id: Int)
}