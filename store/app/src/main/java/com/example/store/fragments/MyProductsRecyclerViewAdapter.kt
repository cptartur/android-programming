package com.example.store.fragments

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import com.example.store.databinding.FragmentItemBinding
import com.example.store.models.Product

class MyProductsRecyclerViewAdapter(
    private val values: MutableList<Product>
) : RecyclerView.Adapter<MyProductsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.name.text = item.name
        holder.description.text = item.description
        holder.price.text = "$" + item.price
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.name
        val description: TextView = binding.description
        val price: TextView = binding.price

        override fun toString(): String {
            return super.toString() + " '" + name.text + "'"
        }
    }

}