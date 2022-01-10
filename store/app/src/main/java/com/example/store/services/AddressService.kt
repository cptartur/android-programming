package com.example.store.services

import com.example.store.models.Address
import retrofit2.Call
import retrofit2.http.*

interface AddressService {

    @GET("address")
    suspend fun getAddresses(): List<Address>

    @GET("address/{id}")
    suspend fun getAddressByID(@Path("id") id: Int): Address

    @POST("address")
    suspend fun createAddress(@Body address: Address)

    @PUT("address/{id}")
    suspend fun updateAddress(@Path("id") id: Int, @Body address: Address)

    @DELETE("address/{id}")
    suspend fun deleteAddress(@Path("id") id: Int)
}