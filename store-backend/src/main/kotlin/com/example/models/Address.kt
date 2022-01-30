package com.example.models

import com.example.tables.Addresses
import org.jetbrains.exposed.sql.ResultRow

data class Address(
    var id: Int,
    var streetAddress: String,
    var postalCode: String,
    var city: String,
    var phoneNumber: String,
    var userId: Int,
) {
    companion object {
        fun fromRow(row: ResultRow) = Address(
            id = row[Addresses.id].value,
            streetAddress = row[Addresses.streetAddress],
            postalCode = row[Addresses.postalCode],
            city = row[Addresses.city],
            phoneNumber = row[Addresses.phoneNumber],
            userId = row[Addresses.userId],
        )
    }
}
