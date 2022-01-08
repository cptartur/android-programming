package com.example.store.repositories

import com.example.store.models.Address
import com.example.store.models.Product
import com.example.store.models.User
import com.example.store.services.AddressService
import com.example.store.services.ProductService
import com.example.store.services.UserService
import retrofit2.Callback

object AddressRepository {
    private val service = RetrofitBuilder.buildService(AddressService::class.java) as AddressService

    fun getAddresses(callback: Callback<List<Address>>) {
        val call = service.getAddresses()
        call.enqueue(callback)
    }

    fun getAddress(id: Int, callback: Callback<Address?>) {
        val call = service.getAddressByID(id)
        call.enqueue(callback)
    }

    fun createAddress(address: Address, callback: Callback<Address>) {
        val call = service.createAddress(address)
        call.enqueue(callback)
    }

    fun updateAddress(id: Int, address: Address, callback: Callback<Address>) {
        val call = service.updateAddress(id, address)
        call.enqueue(callback)
    }

    fun deleteAddress(id: Int, callback: Callback<Address>) {
        val call = service.deleteAddress(id)
        call.enqueue(callback)
    }
}