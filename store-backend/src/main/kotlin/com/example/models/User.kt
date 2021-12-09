package com.example.models

import com.example.tables.Users
import org.jetbrains.exposed.sql.ResultRow

data class User(
    var name: String,
    var id: Int,
) {
    companion object {
        fun fromRow(row: ResultRow) = User(
            name = row[Users.name],
            id = row[Users.id].value
        )
    }
}
