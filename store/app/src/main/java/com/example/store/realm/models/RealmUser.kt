package com.example.store.realm.models

import com.example.store.models.UserType
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class RealmUser(): RealmObject() {
    @Required
    var name: String = ""

    @PrimaryKey
    var id: Int = 0

    @Required
    var email: String = ""

    @Required
    var password: String? = ""

    @Required
    var type: String = ""
}