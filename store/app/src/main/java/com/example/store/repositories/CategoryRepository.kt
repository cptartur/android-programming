package com.example.store.repositories

import com.example.store.models.Category
import com.example.store.models.Product
import com.example.store.services.CategoryService
import com.example.store.services.ProductService
import retrofit2.Callback

object CategoryRepository {
    private val service = RetrofitBuilder.buildService(CategoryService::class.java) as CategoryService

    fun getCategory(callback: Callback<List<Category>>) {
        val call = service.getCategories()
        call.enqueue(callback)
    }

    fun getCategory(id: Int, callback: Callback<Category?>) {
        val call = service.getCategoryByID(id)
        call.enqueue(callback)
    }
}