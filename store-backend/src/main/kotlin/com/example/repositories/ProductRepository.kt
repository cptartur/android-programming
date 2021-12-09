package com.example.repositories

import com.example.models.Product
import com.example.models.User
import com.example.tables.Products
import com.example.tables.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.reflect.typeOf

object ProductRepository : Repository<Product> {

    override fun create(product: Product): Int {
        val id = transaction {
            Products.insertAndGetId {
                it[name] = product.name
                it[description] = product.description
                it[price] = product.price
                it[categoryId] = product.categoryId
            }
        }
        return id.value
    }

    override fun update(id: Int, product: Product): Boolean {
        transaction {
            Products.update({Products.id eq id}) {
                it[name] = product.name
                it[description] = product.description
                it[price] = product.price
                it[Products.id] = Products.select { Products.id eq id }.first()[Products.id]
                it[categoryId] = product.categoryId
            }
        }
        return true
    }

    override fun remove(id: Int): Boolean {
        val status = transaction {
            Products.deleteWhere { Products.id eq id }
        }
        return status == 1
    }

    override fun findById(id: Int): Product? {
        val resultRow = transaction {
            Products.select { Products.id eq id }.firstOrNull()
        }
        return resultRow?.let { Product.fromRow(it) }
    }

    override fun findAll(): List<Product> {
        return transaction {
            Products.selectAll().map { Product.fromRow(it) }
        }
    }
}