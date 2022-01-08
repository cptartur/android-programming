package com.example.store.realm.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class UserRealm(): RealmObject() {
    @Required
    var name: String = ""

    @PrimaryKey
    var id: Int = 0
}