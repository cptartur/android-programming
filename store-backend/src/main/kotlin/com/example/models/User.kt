package com.example.models

import com.example.tables.Users
import org.jetbrains.exposed.sql.ResultRow
import java.security.MessageDigest
import kotlin.random.Random

data class User(
    var name: String,
    var id: Int,
    var email: String?,
    var password: String?,
    var type: UserType
) {
    fun validatePassword(password: String): Boolean {
        val hash = generatePasswordHash(password)
        return hash == this.password
    }

    companion object {
        fun fromRow(row: ResultRow) = User(
            name = row[Users.name],
            id = row[Users.id].value,
            email = row[Users.email],
            password = row[Users.password],
            type = UserType.valueOf(row[Users.type])
        )

        fun generatePasswordHash(password: String): String {
            val md = MessageDigest.getInstance("SHA-256")
            val hash = md.digest(password.toByteArray())
            return hash.joinToString(separator = "") { byte -> "%02x".format(byte) }
        }
    }
}

enum class UserType {
    LOCAL,
    GOOGLE
}
