package com.example.store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.store.databinding.ActivityLoginBinding
import com.example.store.models.Product
import com.example.store.models.User
import com.example.store.realm.repositories.RealmUserRepository
import com.example.store.repositories.ProductRepository
import com.example.store.repositories.UserRepository
import io.realm.Realm
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityLoginBinding  = DataBindingUtil.setContentView(this, R.layout.activity_login)
        Realm.init(this)
        val job = CoroutineScope(Dispatchers.IO)
        job.launch {
            RealmUserRepository.syncUsers()
        }
    }

    fun onClickRegister(view: View) {
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
    }

    fun onClickLogin(view: View) {
        val intent = Intent(this, Products::class.java)
        startActivity(intent)
    }
}