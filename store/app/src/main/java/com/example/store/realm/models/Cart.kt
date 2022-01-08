package com.example.store.realm.models

import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

class Cart {
    @PrimaryKey
    var id: Int = 0

    var userId: Int = 0
}