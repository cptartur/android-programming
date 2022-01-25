package com.example.store.fragments

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.store.databinding.FragmentCartBinding
import com.example.store.realm.models.RealmProduct


class MyCartRecyclerViewAdapter(
    private val values: List<RealmProduct>,
    private val listener: OnRemoveItemListener
) : RecyclerView.Adapter<MyCartRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentCartBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentCartBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.name
        val price = binding.price
        val buttonRemove = binding.buttonRemove

        fun bind(product: RealmProduct) {
            name.text = product.name
            price.text = "$${product.price}"
            buttonRemove.setOnClickListener { listener.onRemoveItem(product, bindingAdapterPosition) }
        }

        override fun toString(): String {
            return super.toString() + " '" + name.text + "'"
        }
    }

}

interface OnRemoveItemListener {
    fun onRemoveItem(item: RealmProduct, position: Int)
}