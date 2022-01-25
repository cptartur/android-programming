package com.example.store

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.store.databinding.ActivityLoginBinding
import com.example.store.models.Product
import com.example.store.models.User
import com.example.store.realm.repositories.RealmUserRepository
import com.example.store.repositories.ProductRepository
import com.example.store.repositories.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.stripe.android.PaymentConfiguration
import io.realm.Realm
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    lateinit var signInClient: GoogleSignInClient
    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val token = account.idToken
                if (token != null) {
                    Log.d("token", token)
                }
                TokenManager.addGoogleAccount(this)
                val intent = Intent(this, Products::class.java)
                startActivity(intent)
            } catch (e: ApiException) {
                Log.w("Sign in", "Sign in failed" + e.statusCode)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityLoginBinding  = DataBindingUtil.setContentView(this, R.layout.activity_login)
        Realm.init(this)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.serverClientId)
            .requestEmail()
            .build()
        signInClient = GoogleSignIn.getClient(this, gso)
        signInClient.signOut()
        PaymentConfiguration.init(this, BuildConfig.stripeApiKey)
    }

    fun onClickGoogleSignIn(view: View) {
        val signInIntent = signInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }
}