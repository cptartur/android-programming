package com.example.repositories

import com.example.models.Product
import com.example.models.User
import com.example.tables.Products
import com.example.tables.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object UserRepository: Repository<User> {

    override fun create(user: User): Int {
        val id = transaction {
            Users.insertAndGetId {
                it[name] = user.name
            }
        }
        return id.value
    }

    override fun update(id: Int, user: User): Boolean {
        transaction {
            Users.update({Users.id eq id}) {
                it[Users.id] = Users.select { Users.id eq id }.first()[Users.id]
                it[name] = user.name
            }
        }
        return true
    }

    override fun remove(id: Int): Boolean {
        val status = transaction {
            Users.deleteWhere { Users.id eq id }
        }
        return status == 1
    }

    override fun findById(id: Int): User? {
        val resultRow = transaction {
            Users.select { Users.id eq id }.firstOrNull()
        }
        return resultRow?.let { User.fromRow(it) }
    }

    override fun findAll(): List<User> {
        return transaction {
            Users.selectAll().map { User.fromRow(it) }
        }
    }

}