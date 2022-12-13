package com.example.repositories

import com.example.models.User
import com.example.models.UserType
import com.example.tables.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object UserRepository: Repository<User> {

    override fun create(obj: User): Int {
        val id = transaction {
            Users.insertAndGetId {
                it[name] = obj.name
                it[email] = obj.email ?: ""
                it[password] = if (obj.type == UserType.LOCAL) User.generatePasswordHash(obj.password!!) else ""
                it[type] = obj.type.toString()
            }
        }
        return id.value
    }

    override fun update(id: Int, obj: User): Boolean {
        transaction {
            Users.update({Users.id eq id}) {
                it[Users.id] = Users.select { Users.id eq id }.first()[Users.id]
                it[name] = obj.name
                it[email] = obj.email ?: ""
                it[password] = if (obj.type == UserType.LOCAL) User.generatePasswordHash(obj.password!!) else ""
                it[type] = obj.type.toString()
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

    fun findByEmail(email: String): User? {
        val resultRow = transaction {
            Users.select { Users.email eq email }.firstOrNull()
        }
        return resultRow?.let { User.fromRow(it) }
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