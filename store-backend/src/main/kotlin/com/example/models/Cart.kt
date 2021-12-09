package com.example.models

import com.example.tables.Carts
import org.jetbrains.exposed.sql.ResultRow

data class Cart(
    val id: Int,
    val userId: Int
) {
    companion object {
        fun fromRow(row: ResultRow) = Cart(
            id = row[Carts.id].value,
            userId = row[Carts.userId]
        )
    }
}