package com.example.store.models

data class Address(
    var id: Int,
    var streetAddress: String,
    var postalCode: String,
    var city: String,
    var phoneNumber: String,
    var userId: Int,
)
