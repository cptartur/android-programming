package com.example.store.services

import com.example.store.models.Category
import retrofit2.Call
import retrofit2.http.*

interface CategoryService {

    @GET("category")
    fun getCategories(): Call<List<Category>>

    @GET("category/{id}")
    fun getCategoryByID(@Path("id") id: Int): Call<Category>

    @POST("category")
    fun createCategory(@Body category: Category): Call<Category>

    @PUT("category/{id}")
    fun updateCategory(@Path("id") id: Int, @Body category: Category): Call<Category>

    @DELETE("category/{id}")
    fun deleteCategory(@Path("id") id: Int): Call<Category>
}