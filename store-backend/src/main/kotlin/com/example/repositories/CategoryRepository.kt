package com.example.repositories

import com.example.models.Category
import com.example.tables.Categories
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object CategoryRepository : Repository<Category> {

    override fun create(obj: Category): Int {
        val id = transaction {
            Categories.insertAndGetId {
                it[name] = obj.name
            }
        }
        return id.value
    }

    override fun update(id: Int, obj: Category): Boolean {
        transaction {
            Categories.update({Categories.id eq id}) {
                it[Categories.id] = Categories.select { Categories.id eq id }.first()[Categories.id]
                it[name] = obj.name
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

    fun findByName(name: String): Category? {
        val resultRow = transaction {
            Categories.select { Categories.name eq name }.firstOrNull()
        }
        return resultRow?.let { Category.fromRow(it) }
    }

    override fun findAll(): List<Category> {
        return transaction {
            Categories.selectAll().map { Category.fromRow(it) }
        }
    }
}