package com.example.store.models

data class User(
    var name: String,
    var id: Int,
    var email: String?,
    var password: String?,
    var type: UserType
)

enum class UserType {
    LOCAL,
    GOOGLE
}