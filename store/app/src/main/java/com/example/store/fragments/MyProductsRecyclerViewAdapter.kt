package com.example.store.fragments

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.store.databinding.FragmentProductBinding
import com.example.store.realm.models.RealmCategory
import com.example.store.realm.models.RealmProduct

class MyProductsRecyclerViewAdapter(
    private var values: MutableList<RealmProduct>,
    private val categories: List<RealmCategory>,
    private val listener: OnAddToCartClickListener,
    private val detailsListener: OnDetailsClickListener,
) : RecyclerView.Adapter<MyProductsRecyclerViewAdapter.ViewHolder>() {
    private val originalValues: MutableList<RealmProduct> = values
    private var valuesCopy: MutableList<RealmProduct> = values
    private var filterString: String = ""

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

    inner class ViewHolder(binding: FragmentProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.name
        val category: TextView = binding.category
        val price: TextView = binding.price
        val addToCartButton = binding.buttonAddToCart
        val view = binding.root

        override fun toString(): String {
            return super.toString() + " '" + name.text + "'"
        }

        fun bind(product: RealmProduct, listener: OnAddToCartClickListener) {
            name.text = product.name
            category.text =
                categories.firstOrNull { it.id == product.categoryId }?.name ?: "Unknown category"
            price.text = "$" + product.price
            addToCartButton.setOnClickListener { listener.onAddToCart(product) }
//            name.setOnClickListener { detailsListener.onDetails(product) }
            view.setOnClickListener { detailsListener.onDetails(product) }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filter(query: String) {
        filterString = query
        values =
            valuesCopy.filter { it.name.lowercase().startsWith(query.lowercase()) }.toMutableList()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterByCategory(categoryId: Int?) {
        val filteredValues =
            categoryId?.let { originalValues.filter { it.categoryId == categoryId } }
        values = filteredValues?.toMutableList() ?: originalValues
        valuesCopy = values
        filter(filterString)
        notifyDataSetChanged()
    }
}

interface OnAddToCartClickListener {
    fun onAddToCart(product: RealmProduct)
}

interface OnDetailsClickListener {
    fun onDetails(product: RealmProduct)
}