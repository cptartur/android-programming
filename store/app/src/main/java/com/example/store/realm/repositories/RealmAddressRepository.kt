package com.example.store.realm.repositories

import com.example.store.realm.RealmConfig
import com.example.store.realm.models.RealmAddress
import com.example.store.repositories.AddressRepository
import io.realm.Realm

object RealmAddressRepository {
    suspend fun syncAddresses() {
        val realm = Realm.getInstance(RealmConfig.realmConfig())
        val addresses = AddressRepository.getAddresses()
        realm.executeTransaction {
            it.delete(RealmAddress::class.java)
        }
        addresses.forEach {
            val address = RealmAddress()
            address.id = it.id
            address.city = it.city
            address.phoneNumber = it.phoneNumber
            address.postalCode = it.postalCode
            address.streetAddress = it.streetAddress
            address.userId = it.userId
            realm.executeTransaction { realmTransaction ->
                realmTransaction.insert(address)
            }
        }
    }

    fun getAddresses(): List<RealmAddress> {
        val realm = Realm.getInstance(RealmConfig.realmConfig())
        val result = realm.where(RealmAddress::class.java).findAll()
        return realm.copyFromRealm(result)
    }

    fun getAddress(id: Int): RealmAddress? {
        val realm = Realm.getInstance(RealmConfig.realmConfig())
        return realm.where(RealmAddress::class.java)
            .equalTo("id", id)
            .findFirst()
    }
}