package com.example.store

import android.content.Context
import android.util.Log
import com.example.store.models.UserType
import com.example.store.repositories.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

object TokenManager {

    var type: UserType = UserType.LOCAL

    fun getAuthToken(context: Context): String? {
        var authToken: String? = null
        when (type) {
            UserType.GOOGLE -> {
                val account = GoogleSignIn.getLastSignedInAccount(context) ?: return null
                runBlocking(Dispatchers.IO) {
                    Log.d("dupa", "dupa2")
                    authToken = AuthRepository.login(account.idToken!!).token
                }
            }
            UserType.LOCAL -> {
                val sharedPerf = context.getSharedPreferences("user", Context.MODE_PRIVATE)
                val email = sharedPerf.getString("email", "")!!
                val password = sharedPerf.getString("password", "")!!
                runBlocking(Dispatchers.IO) {
                    try {
                        authToken = AuthRepository.localLogin(email, password).token
                    } catch (ex: HttpException) {
                        ex.printStackTrace()
                        return@runBlocking
                    }
                }
            }
        }
        return authToken
    }

    fun addLocalAccount(context: Context, email: String, password: String) {
        with (context.getSharedPreferences("user", Context.MODE_PRIVATE).edit()) {
            putString("email", email)
            putString("password", password)
        }
    }

    fun addGoogleAccount(context: Context) {
        type = UserType.GOOGLE
    }
}