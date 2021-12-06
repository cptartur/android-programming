package com.example.repositories

import com.example.models.Address
import com.example.tables.Addresses
import com.example.tables.Categories
import com.example.tables.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object AddressRepository : Repository<Address> {

    override fun create(address: Address): Int {
        val id = transaction {
            Addresses.insertAndGetId {
                it[id] = address.id
                it[streetAddress] = address.streetAddress
                it[postalCode] = address.postalCode
                it[city] = address.city
                it[phoneNumber] = address.phoneNumber
            }
        }
        return id.value
    }

    override fun update(id: Int, address: Address): Boolean {
        transaction {
            Addresses.update({Users.id eq id}) {
                it[Addresses.id] = address.id
                it[streetAddress] = address.streetAddress
                it[postalCode] = address.postalCode
                it[city] = address.city
                it[phoneNumber] = address.phoneNumber
            }
        }
        return true
    }

    override fun remove(id: Int): Boolean {
        val status = transaction {
            Addresses.deleteWhere { Addresses.id eq id }
        }
        return status == 1
    }

    override fun findById(id: Int): Address? {
        val resultRow = transaction {
            Addresses.select { Addresses.id eq id }.firstOrNull()
        }
        return resultRow?.let { Address.fromRow(it) }
    }

    override fun findAll(): List<Address> {
        return transaction {
            Addresses.selectAll().map { Address.fromRow(it) }
        }
    }
}