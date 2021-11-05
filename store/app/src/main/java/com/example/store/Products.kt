package com.example.store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView

class Products : AppCompatActivity() {
    private lateinit var products: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        val listView = findViewById<ListView>(R.id.products)
        products = listOf("aaaa", "bbbb", "cccc", "cccc", "cccc", "cccc", "cccc", "cccc", "cccc", "cccc", "cccc")

        listView.adapter = ProductAdapter(this, products)
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