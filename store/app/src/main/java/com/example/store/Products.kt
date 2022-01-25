package com.example.store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.store.databinding.ActivityProducts2Binding
import com.example.store.realm.repositories.RealmCartRepository
import com.example.store.realm.repositories.RealmProductRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class Products : AppCompatActivity() {
    private lateinit var products: List<String>
    private lateinit var binding: ActivityProducts2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_products2)

        runBlocking {
            withContext(Dispatchers.IO) {
                RealmProductRepository.syncProducts()
                RealmCartRepository.removeCart(0)
                RealmCartRepository.createCart(0)
            }
        }

        binding.goToCartFloatingButton.setOnClickListener { onClick(it) }
        binding.buttonLocations.setOnClickListener { onClick(it) }
    }

    private fun onClick(view: View) {
        when (view.id) {
            R.id.goToCartFloatingButton -> startCartActivity()
            R.id.buttonLocations -> startLocationsActivity()
        }
    }

    private fun startCartActivity() {
        Intent(this, Cart::class.java).apply {
            startActivity(this)
        }
    }

    private fun startLocationsActivity() {
        val intent = Intent(this, LocationsActivity::class.java)
        startActivity(intent)
    }
}