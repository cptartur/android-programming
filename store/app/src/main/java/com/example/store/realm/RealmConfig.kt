package com.example.store.realm

import io.realm.RealmConfiguration

object RealmConfig {
    private const val realmVersion = 1L
    private const val realmName = "StoreRealm"

    fun realmConfig(): RealmConfiguration {
        return RealmConfiguration.Builder().name(realmName).schemaVersion(realmVersion).build()
    }
}