package com.example.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object Carts : IntIdTable() {
    val userId = integer("user_id").references(Users.id)
}