package com.example.store.repositories

import com.example.store.models.Product
import com.example.store.models.User
import com.example.store.services.ProductService
import com.example.store.services.UserService
import retrofit2.Callback

object UserRepository {
    private val service = RetrofitBuilder.buildService(UserService::class.java) as UserService

//    suspend fun getUsers(): List<User> {
//        return service.getUsers()
//    }
//
//    fun getUser(id: Int): User {
//        return service.getUserByID(id)
//    }
//
//    fun createUser(user: User, callback: Callback<User>) {
//        val call = service.createUser(user)
//        call.enqueue(callback)
//    }
//
//    fun updateUser(id: Int, user: User, callback: Callback<User>) {
//        val call = service.updateUser(id, user)
//        call.enqueue(callback)
//    }
//
//    fun deleteUser(id: Int, callback: Callback<User>) {
//        val call = service.deleteUser(id)
//        call.enqueue(callback)
//    }

    suspend fun getUsers(): List<User> {
        return service.getUsers()
    }

    suspend fun getUser(id: Int): User {
        return service.getUserByID(id)
    }

    suspend fun createUser(user: User) {
        return service.createUser(user)
    }

    suspend fun updateUser(id: Int, user: User) {
        return service.updateUser(id, user)
    }

    suspend fun deleteUser(id: Int) {
        return service.deleteUser(id)
    }
}