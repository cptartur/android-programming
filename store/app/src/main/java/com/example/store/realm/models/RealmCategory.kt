package com.example.store.realm.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class RealmCategory(): RealmObject() {
    @PrimaryKey
    var id: Int = 0

    @Required
    var name: String = ""

    override fun toString() = name
}