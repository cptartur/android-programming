package com.example.store.services

import com.example.store.models.User
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @GET("user")
    fun getUsers(): Call<List<User>>

    @GET("user/{id}")
    fun getUserByID(@Path("id") id: Int): Call<User>

    @POST("user")
    fun createUser(@Body user: User): Call<User>

    @PUT("user/{id}")
    fun updateUser(@Path("id") id: Int, @Body user: User): Call<User>
    
    @DELETE("user/{id}")
    fun deleteUser(@Path("id") id: Int): Call<User>
}