package com.example.repositories

import com.example.models.Address
import com.example.tables.Addresses
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object AddressRepository : Repository<Address> {

    override fun create(obj: Address): Int {
        val id = transaction {
            Addresses.insertAndGetId {
                it[id] = obj.id
                it[streetAddress] = obj.streetAddress
                it[postalCode] = obj.postalCode
                it[city] = obj.city
                it[phoneNumber] = obj.phoneNumber
                it[userId] = obj.userId
            }
        }
        return id.value
    }

    override fun update(id: Int, obj: Address): Boolean {
        transaction {
            Addresses.update({Addresses.id eq id}) {
                it[Addresses.id] = Addresses.select {Addresses.id eq id}.first()[Addresses.id]
                it[streetAddress] = obj.streetAddress
                it[postalCode] = obj.postalCode
                it[city] = obj.city
                it[phoneNumber] = obj.phoneNumber
                it[userId] = obj.userId
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