package com.example.store.realm.repositories

import android.util.Log
import com.example.store.realm.RealmConfig
import com.example.store.realm.models.RealmCart
import com.example.store.realm.models.RealmProduct
import io.realm.Realm
import io.realm.RealmList

object RealmCartRepository {

    fun getCart(userId: Int): RealmCart? {
        val realm = Realm.getInstance(RealmConfig.realmConfig())
        val cart = realm.where(RealmCart::class.java)
            .equalTo("userId", userId)
            .findFirst()
        return realm.copyFromRealm(cart)
    }

//    fun getCart(userId: Int, cartId: Int): RealmCart? {
//        val realm = Realm.getInstance(RealmConfig.realmConfig())
//        return realm.where(RealmCart::class.java)
//            .equalTo("userId", userId)
//            .equalTo("id", cartId)
//            .findFirst()
//    }

    fun createCart(userId: Int) {
        val realm = Realm.getInstance(RealmConfig.realmConfig())
        realm.executeTransaction { transitionRealm ->
            val cart = RealmCart()
//            cart.id = cartId
            cart.userId = userId
            cart.products = RealmList()
            transitionRealm.insert(cart)
        }
    }

    fun updateCart(userId: Int, products: List<RealmProduct>) {
        val realm = Realm.getInstance(RealmConfig.realmConfig())
        realm.executeTransaction {
            val cart = it.where(RealmCart::class.java)
                .equalTo("userId", userId)
                .findFirst()!!
            val realmList = RealmList<RealmProduct>()
            products.forEach { product ->
                realmList.add(product)
            }
            cart.products = realmList
        }
    }

    fun addToCart(userId: Int, product: RealmProduct) {
        val realm = Realm.getInstance(RealmConfig.realmConfig())
        realm.executeTransaction { realmTransaction ->
            val cart = realmTransaction.where(RealmCart::class.java)
                .equalTo("userId", userId)
                .findFirst()!!
            cart.products?.let { it.add(product) } ?: run {cart.products = RealmList(product) }
        }
    }

    fun removeFromCart(userId: Int, product: RealmProduct) {
        val realm = Realm.getInstance(RealmConfig.realmConfig())
        realm.executeTransaction { realmTransaction ->
            val cart = realmTransaction.where(RealmCart::class.java)
                .equalTo("userId", userId)
                .findFirst()
            Log.d("realm removing",
                cart?.products?.removeAll { realmProduct -> realmProduct.id == product.id }.toString()
            )
        }
    }

    fun removeCart(userId: Int) {
        val realm = Realm.getInstance(RealmConfig.realmConfig())
        realm.executeTransaction { realmTransaction ->
            val cart = realmTransaction.where(RealmCart::class.java)
                .equalTo("userId", userId)
                .findFirst()
            cart?.deleteFromRealm()
        }
    }
}