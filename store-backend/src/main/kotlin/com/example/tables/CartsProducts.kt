package com.example.tables

import org.jetbrains.exposed.sql.Table

object CartsProducts : Table() {
    val cartId = integer("cart_id").references(Carts.id)
    val productId = integer("product_id").references(Users.id)
    val amount = integer("amount")

    override val primaryKey = PrimaryKey(cartId, productId, name = "PK_CartsProducts")
}