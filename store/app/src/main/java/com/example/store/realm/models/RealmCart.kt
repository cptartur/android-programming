package com.example.store.realm.models

import io.realm.RealmList
import io.realm.RealmObject

open class RealmCart(): RealmObject() {
//    @PrimaryKey
//    var id: Int = 0

    var userId: Int = 0

    var products: RealmList<RealmProduct>? = null
}