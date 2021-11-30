package com.example.models

import com.example.tables.Products
import org.jetbrains.exposed.sql.ResultRow

data class Product(
    var name: String,
    var description: String,
    var price: Int,
    var id: Int,
) {
    companion object {
        fun fromRow(row: ResultRow) = Product(
            name = row[Products.name],
            description = row[Products.description],
            price = row[Products.price],
            id = row[Products.id].value
        )
    }
}