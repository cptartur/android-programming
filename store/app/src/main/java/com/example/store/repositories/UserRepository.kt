package com.example.store.repositories

import com.example.store.models.Product
import com.example.store.models.User
import com.example.store.services.ProductService
import com.example.store.services.UserService
import retrofit2.Callback

object UserRepository {
    private val service = RetrofitBuilder.buildService(UserService::class.java) as UserService

    fun getUsers(callback: Callback<List<User>>) {
        val call = service.getUsers()
        call.enqueue(callback)
    }

    fun getUser(id: Int, callback: Callback<User?>) {
        val call = service.getUserByID(id)
        call.enqueue(callback)
    }

    fun createUser(user: User, callback: Callback<User>) {
        val call = service.createUser(user)
        call.enqueue(callback)
    }

    fun updateUser(id: Int, user: User, callback: Callback<User>) {
        val call = service.updateUser(id, user)
        call.enqueue(callback)
    }

    fun deleteUser(id: Int, callback: Callback<User>) {
        val call = service.deleteUser(id)
        call.enqueue(callback)
    }
}