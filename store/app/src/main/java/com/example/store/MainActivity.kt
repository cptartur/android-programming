package com.example.store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.store.databinding.ActivityLoginBinding
import com.example.store.models.Product
import com.example.store.repositories.ProductRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityLoginBinding  = DataBindingUtil.setContentView(this, R.layout.activity_login)
//        Realm.init(this)
        val callback = object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful && response.body() != null) {
                    for (product in response.body()!!) {
                        println(product.toString())
                    }
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                return
            }
        }
        ProductRepository.getProducts(callback)
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