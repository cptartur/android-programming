package com.example.repositories

import com.example.models.Category
import com.example.models.Product
import com.example.models.User
import com.example.tables.Categories
import com.example.tables.Products
import com.example.tables.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.reflect.typeOf

object CategoryRepository : Repository<Category> {

    override fun create(category: Category): Int {
        val id = transaction {
            Categories.insertAndGetId {
                it[name] = category.name
            }
        }
        return id.value
    }

    override fun update(id: Int, category: Category): Boolean {
        transaction {
            Categories.update({Categories.id eq id}) {
                it[Categories.id] = Categories.select { Categories.id eq id }.first()[Categories.id]
                it[name] = category.name
            }
        }
        return true
    }

    override fun remove(id: Int): Boolean {
        val status = transaction {
            Categories.deleteWhere { Categories.id eq id }
        }
        return status == 1
    }

    override fun findById(id: Int): Category? {
        val resultRow = transaction {
            Categories.select { Categories.id eq id }.firstOrNull()
        }
        return resultRow?.let { Category.fromRow(it) }
    }

    override fun findAll(): List<Category> {
        return transaction {
            Categories.selectAll().map { Category.fromRow(it) }
        }
    }
}