package com.example.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object Addresses: IntIdTable() {
    val streetAddress = varchar("street_address", 120)
    val postalCode = varchar("postal_code", 7)
    val city = varchar("city", 50)
    val phoneNumber = varchar("phone_number", 14)
}