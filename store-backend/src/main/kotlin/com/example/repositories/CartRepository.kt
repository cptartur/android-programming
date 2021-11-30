package com.example.repositories

import com.example.models.Cart
import com.example.tables.Carts
import com.example.tables.Products
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object CartRepository: Repository<Cart> {
    override fun create(obj: Cart): Int {
        val id = transaction {
            Carts.insertAndGetId {
                it[userId] = obj.userId
            }
        }
        return id.value
    }

    fun create(userId: Int): Int {
        val id = transaction {
            Carts.insertAndGetId {
                it[Carts.userId] = userId
            }
        }
        return id.value
    }

    override fun update(id: Int, obj: Cart): Boolean {
        val status = transaction {
            Carts.update({ Carts.id eq id}) {
                it[userId] = obj.userId
            }
        }
        return status == 1
    }

    override fun remove(id: Int): Boolean {
        val status = transaction {
            Carts.deleteWhere { Carts.id eq id }
        }
        return status == 1
    }

    override fun findById(id: Int): Cart? {
        val resultRow = transaction {
            Carts.select { Carts.userId eq id }.firstOrNull()
        }
        return resultRow?.let { Cart.fromRow(it) }
    }

    override fun findAll(): List<Cart> {
        return transaction {
            Carts.selectAll().map { Cart.fromRow(it) }
        }
    }
}