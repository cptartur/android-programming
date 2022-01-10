package com.example.store.realm.repositories

import com.example.store.realm.RealmConfig
import com.example.store.realm.models.RealmUser
import com.example.store.repositories.UserRepository
import io.realm.Realm

object RealmUserRepository {
    suspend fun syncUsers() {
        val realm = Realm.getInstance(RealmConfig.realmConfig())
        val users = UserRepository.getUsers()
        realm.executeTransaction {
            it.delete(RealmUser::class.java)
        }
        users.forEach {
            val user = RealmUser()
            user.id = it.id
            user.name = it.name
            realm.executeTransaction { realmTransaction ->
                realmTransaction.insert(user)
            }
        }
    }

    fun getUsers(): List<RealmUser> {
        val realm = Realm.getInstance(RealmConfig.realmConfig())
        val result = realm.where(RealmUser::class.java).findAll()
        return realm.copyFromRealm(result)
    }

    fun getUser(id: Int): RealmUser? {
        val realm = Realm.getInstance(RealmConfig.realmConfig())
        return realm.where(RealmUser::class.java)
            .equalTo("id", id)
            .findFirst()
    }
}