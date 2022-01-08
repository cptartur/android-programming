package com.example.store.repositories

import com.example.store.models.Product
import com.example.store.services.ProductService
import retrofit2.Callback

object ProductRepository {
    private val service = RetrofitBuilder.buildService(ProductService::class.java) as ProductService

    fun getProducts(callback: Callback<List<Product>>) {
        val call = service.getProducts()
        call.enqueue(callback)
    }

    fun getProduct(id: Int, callback: Callback<Product?>) {
        val call = service.getProductByID(id)
        call.enqueue(callback)
    }
}