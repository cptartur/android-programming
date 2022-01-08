package com.example.store.realm.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.store.models.User
import com.example.store.realm.RealmConfig
import com.example.store.realm.models.UserRealm
import com.example.store.repositories.UserRepository
import io.realm.Realm
import io.realm.kotlin.delete
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RealmUserRepository {
    suspend fun syncUsers() {
        withContext(Dispatchers.IO) {
            val realm = Realm.getInstance(RealmConfig.realmConfig())
            val users = UserRepository.getUsers()
            realm.executeTransaction {
                it.delete(UserRealm::class.java)
            }
            users.forEach {
                val user = UserRealm()
                user.id = it.id
                user.name = it.name
                realm.executeTransaction { realmTransaction ->
                    realmTransaction.insert(user)
                }
            }
        }
    }
}