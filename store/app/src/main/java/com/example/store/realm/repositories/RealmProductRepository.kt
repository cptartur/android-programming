package com.example.store.realm.repositories

import com.example.store.realm.RealmConfig
import com.example.store.realm.models.RealmProduct
import com.example.store.repositories.ProductRepository
import io.realm.Realm

object RealmProductRepository {
    suspend fun syncProducts() {
        val realm = Realm.getInstance(RealmConfig.realmConfig())
        val products = ProductRepository.getProducts()
        realm.executeTransaction {
            it.delete(RealmProduct::class.java)
        }
        products.forEach {
            val product = RealmProduct()
            product.id = it.id
            product.name = it.name
            product.description = it.description
            product.price = it.price
            product.categoryId = it.categoryId
            product.imageUrl = it.imageUrl
            realm.executeTransaction { realmTransaction ->
                realmTransaction.insert(product)
            }
        }
    }

    fun getProducts(): List<RealmProduct> {
        val realm = Realm.getInstance(RealmConfig.realmConfig())
        val result = realm.where(RealmProduct::class.java).findAll()
        return realm.copyFromRealm(result)
    }

    fun getProduct(id: Int): RealmProduct? {
        val realm = Realm.getInstance(RealmConfig.realmConfig())
        return realm.where(RealmProduct::class.java)
            .equalTo("id", id)
            .findFirst()
    }
}