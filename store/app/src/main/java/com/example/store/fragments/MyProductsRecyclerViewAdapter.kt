package com.example.store.fragments

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.store.databinding.FragmentProductBinding
import com.example.store.realm.models.RealmProduct

class MyProductsRecyclerViewAdapter(
    private val values: MutableList<RealmProduct>,
    private val listener: OnAddToCartClickListener
) : RecyclerView.Adapter<MyProductsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = values[position]
        holder.bind(product, listener)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentProductBinding) : RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.name
        val description: TextView = binding.description
        val price: TextView = binding.price
        val addToCartButton = binding.buttonAddToCart

        override fun toString(): String {
            return super.toString() + " '" + name.text + "'"
        }

        fun bind(product: RealmProduct, listener: OnAddToCartClickListener) {
            name.text = product.name
            description.text = product.description
            price.text = "$" + product.price
            addToCartButton.setOnClickListener { listener.onAddToCart(product) }
        }
    }
}

interface OnAddToCartClickListener {
    fun onAddToCart(product: RealmProduct)
}