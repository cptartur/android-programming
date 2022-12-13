package com.example.repositories

import com.example.models.Product
import com.example.tables.Products
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object ProductRepository : Repository<Product> {

    override fun create(obj: Product): Int {
        val id = transaction {
            Products.insertAndGetId {
                it[name] = obj.name
                it[description] = obj.description
                it[price] = obj.price
                it[categoryId] = obj.categoryId
            }
        }
        return id.value
    }

    override fun update(id: Int, obj: Product): Boolean {
        transaction {
            Products.update({Products.id eq id}) {
                it[name] = obj.name
                it[description] = obj.description
                it[price] = obj.price
                it[Products.id] = Products.select { Products.id eq id }.first()[Products.id]
                it[categoryId] = obj.categoryId
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