package com.example.store.realm.models

import com.example.store.models.Product
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class RealmCart(): RealmObject() {
//    @PrimaryKey
//    var id: Int = 0

    var userId: Int = 0

    var products: RealmList<RealmProduct>? = null
}