package com.example.repositories

import com.example.models.Cart
import com.example.models.Product
import com.example.models.User
import com.example.tables.Carts
import com.example.tables.CartsProducts
import com.example.tables.Products
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object CartProductRepository {
    fun create(id: Int, product: Product, amount: Int): Int {
        transaction {
            CartsProducts.insert {
                it[cartId] = id
                it[productId] = product.id
                it[CartsProducts.amount] = amount
            }
        }
        return id
    }

    fun updateAmount(cartId: Int, product: Product, amount: Int): Boolean {
        val status = transaction {
            CartsProducts.update({ CartsProducts.cartId eq cartId }) {
                it[CartsProducts.amount] = amount
            }
        }
        return status == 1
    }

    fun remove(id: Int): Boolean {
        val status = transaction {
            CartsProducts.deleteWhere { CartsProducts.cartId eq id }
        }
        return status == 1
    }

    fun findById(cartId: Int): List<Pair<Product, Int>>? {
        return CartsProducts.join(Products, JoinType.INNER, additionalConstraint = { CartsProducts.cartId eq cartId })
            .slice(Products.id, Products.price, Products.name, Products.description, CartsProducts.amount)
            .selectAll()
            .map {
                Pair(Product(it[Products.name], it[Products.description], it[Products.price], it[Products.id].value), it[CartsProducts.amount]) }
    }
}