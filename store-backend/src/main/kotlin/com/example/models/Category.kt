package com.example.models

import com.example.tables.Categories
import org.jetbrains.exposed.sql.ResultRow

data class Category(
    var id: Int,
    var name: String,
) {
    companion object {
        fun fromRow(row: ResultRow) = Category(
            id = row[Categories.id].value,
            name = row[Categories.name]
        )
    }
}
