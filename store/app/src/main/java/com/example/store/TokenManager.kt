package com.example.store

import android.content.Context
import com.example.store.models.UserType
import com.example.store.repositories.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

object TokenManager {

    var type: UserType = UserType.LOCAL

    fun getAuthToken(context: Context): String? {
        return when (type) {
            UserType.GOOGLE -> {
                val account = GoogleSignIn.getLastSignedInAccount(context) ?: return null
                runBlocking(Dispatchers.IO) {
                    AuthRepository.login(account.idToken!!).token
                }
            }
            UserType.LOCAL -> {
                val sharedPerf = context.getSharedPreferences("store_app_user", Context.MODE_PRIVATE)
                val email = sharedPerf.getString("email", "")!!
                val password = sharedPerf.getString("password", "")!!
                runBlocking(Dispatchers.IO) {
                    try {
                        AuthRepository.localLogin(email, password).token
                    } catch (ex: HttpException) {
                        ex.printStackTrace()
                        null
                    }
                }
            }
        }
    }

    fun addLocalAccount(context: Context, email: String, password: String) {
        with (context.getSharedPreferences("store_app_user", Context.MODE_PRIVATE).edit()) {
            putString("email", email)
            putString("password", password)
            commit()
        }
    }

    fun addGoogleAccount() {
        type = UserType.GOOGLE
    }
}