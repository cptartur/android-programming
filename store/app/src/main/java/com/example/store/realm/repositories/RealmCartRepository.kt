package com.example.store.realm.repositories

import com.example.store.models.Product
import com.example.store.realm.RealmConfig
import com.example.store.realm.models.RealmCart
import com.example.store.realm.models.RealmProduct
import io.realm.Realm
import io.realm.RealmList

object RealmCartRepository {

    fun getCarts(userId: Int): List<RealmCart> {
        val realm = Realm.getInstance(RealmConfig.realmConfig())
        val result = realm.where(RealmCart::class.java)
            .equalTo("userId", userId)
            .findAll()
        return realm.copyFromRealm(result)
    }

    fun getCart(userId: Int, cartId: Int): RealmCart? {
        val realm = Realm.getInstance(RealmConfig.realmConfig())
        return realm.where(RealmCart::class.java)
            .equalTo("userId", userId)
            .equalTo("id", cartId)
            .findFirst()
    }

    fun updateCart(cartId: Int, products: List<RealmProduct>) {
        val realm = Realm.getInstance(RealmConfig.realmConfig())
        realm.executeTransaction {
            val cart = it.where(RealmCart::class.java)
                .equalTo("id", cartId)
                .findFirst()!!
            val realmList = RealmList<RealmProduct>()
            products.forEach { product ->
                realmList.add(product)
            }
            cart.products = realmList
        }
    }
}