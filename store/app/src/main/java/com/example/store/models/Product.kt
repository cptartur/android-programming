package com.example.store.models

data class Product(
    var name: String,
    var description: String,
    var price: Int,
    var id: Int,
    var categoryId: Int,
    var imageUrl: String
)
