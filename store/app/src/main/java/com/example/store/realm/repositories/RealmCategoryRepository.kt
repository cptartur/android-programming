package com.example.store.realm.repositories

import com.example.store.realm.RealmConfig
import com.example.store.realm.models.RealmCategory
import com.example.store.repositories.CategoryRepository
import io.realm.Realm

object RealmCategoryRepository {
    suspend fun syncCategories() {
        val realm = Realm.getInstance(RealmConfig.realmConfig())
        val categories = CategoryRepository.getCategories()
        realm.executeTransaction {
            it.delete(RealmCategory::class.java)
        }
        categories.forEach {
            val category = RealmCategory()
            category.id = it.id
            category.name = it.name
            realm.executeTransaction { realmTransaction ->
                realmTransaction.insert(category)
            }
        }
    }

    fun getCategories(): List<RealmCategory> {
        val realm = Realm.getInstance(RealmConfig.realmConfig())
        val result = realm.where(RealmCategory::class.java).findAll()
        return realm.copyFromRealm(result)
    }

    fun getCategory(id: Int): RealmCategory? {
        val realm = Realm.getInstance(RealmConfig.realmConfig())
        return realm.where(RealmCategory::class.java)
            .equalTo("id", id)
            .findFirst()
    }
}