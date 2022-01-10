package com.example.store.repositories

import com.example.store.models.Category
import com.example.store.services.CategoryService

object CategoryRepository {
    private val service = RetrofitBuilder.buildService(CategoryService::class.java) as CategoryService

    suspend fun getCategories(): List<Category> {
        return service.getCategories()
    }

    suspend fun getCategories(id: Int): Category {
        return service.getCategoryByID(id)
    }
}