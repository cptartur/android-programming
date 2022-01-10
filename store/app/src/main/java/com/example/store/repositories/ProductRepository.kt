package com.example.store.repositories

import com.example.store.models.Product
import com.example.store.services.ProductService

object ProductRepository {
    private val service = RetrofitBuilder.buildService(ProductService::class.java) as ProductService

    suspend fun getProducts(): List<Product> {
        return service.getProducts()
    }

    suspend fun getProduct(id: Int): Product {
        return service.getProductByID(id)
    }
}