package com.example.store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import com.example.store.models.Product
import com.example.store.repositories.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class Products : AppCompatActivity() {
    private lateinit var products: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products2)

//        val listView = findViewById<ListView>(R.id.products)
////        products = listOf("aaaa", "bbbb", "cccc", "cccc", "cccc", "cccc", "cccc", "cccc", "cccc", "cccc", "cccc")
//        val products: List<Product>
//        runBlocking {
//            withContext(Dispatchers.IO) {
//                products = ProductRepository.getProducts()
//            }
//        }
//
//        listView.adapter = ProductAdapter(this, products)
    }

    fun onAddToCart(view: View) {
        val intent = Intent(this, Cart::class.java)
        startActivity(intent)
    }

    fun onAboutMe(view: View) {
        val intent = Intent(this, AboutMe::class.java)
        startActivity(intent)
    }
}