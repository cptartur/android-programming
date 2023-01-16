package com.example.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object Products : IntIdTable() {
    val name = varchar("name", 50)
    val description = varchar("description", 150)
    val price = integer("price")
    val categoryId = integer("category_id").references(Categories.id)
    val imageUrl = varchar("image_url", 180)
}