package com.example.store.realm.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class RealmAddress(): RealmObject() {
    @PrimaryKey
    var id: Int = 0

    @Required
    var streetAddress: String = ""

    @Required
    var postalCode: String = ""

    @Required
    var city: String = ""

    @Required
    var phoneNumber: String = ""

    var userId: Int = 0
}