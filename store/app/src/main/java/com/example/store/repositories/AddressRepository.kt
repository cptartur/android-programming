package com.example.store.repositories

import com.example.store.models.Address
import com.example.store.services.AddressService

object AddressRepository {
    private val service = RetrofitBuilder.buildService(AddressService::class.java) as AddressService

    suspend fun getAddresses(): List<Address> {
        return service.getAddresses()
    }

    suspend fun getAddress(id: Int): Address {
        return service.getAddressByID(id)
    }

    suspend fun createAddress(address: Address) {
        return service.createAddress(address)
    }

    suspend fun updateAddress(id: Int, address: Address) {
        return service.updateAddress(id, address)
    }

    suspend fun deleteAddress(id: Int) {
        return service.deleteAddress(id)
    }
}