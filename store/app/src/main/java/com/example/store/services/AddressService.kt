package com.example.store.services

import com.example.store.models.Address
import retrofit2.Call
import retrofit2.http.*

interface AddressService {

    @GET("address")
    fun getAddresses(): Call<List<Address>>

    @GET("address/{id}")
    fun getAddressByID(@Path("id") id: Int): Call<Address>

    @POST("address")
    fun createAddress(@Body address: Address): Call<Address>

    @PUT("address/{id}")
    fun updateAddress(@Path("id") id: Int, @Body address: Address): Call<Address>

    @DELETE("address/{id}")
    fun deleteAddress(@Path("id") id: Int): Call<Address>
}