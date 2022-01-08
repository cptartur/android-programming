package com.example.store.realm.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class Product(): RealmObject() {
    @PrimaryKey
    var id: Int = 0

    @Required
    var name: String = ""

    @Required
    var description: String = ""

    var price: Int = 0

    var categoryId: Int = 0
}