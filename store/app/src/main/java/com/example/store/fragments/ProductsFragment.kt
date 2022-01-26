package com.example.store.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.R
import com.example.store.models.Product
import com.example.store.realm.models.RealmProduct
import com.example.store.realm.repositories.RealmCartRepository
import com.example.store.realm.repositories.RealmProductRepository
import com.example.store.repositories.ProductRepository
import kotlinx.coroutines.*

/**
 * A fragment representing a list of Items.
 */
class ProductsFragment : Fragment() {

    private var columnCount = 1
    private var products: List<RealmProduct> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_list, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runBlocking(Dispatchers.IO) {
            products = RealmProductRepository.getProducts()
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.list)

        // Set the adapter
        if (recyclerView is RecyclerView) {
            with(recyclerView) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyProductsRecyclerViewAdapter(products as MutableList<RealmProduct>,
                    object : OnAddToCartClickListener {
                        override fun onAddToCart(product: RealmProduct) {
                            addToCart(product)
                        }
                    })
            }
        }
    }

    private fun addToCart(product: RealmProduct) {
        runBlocking {
            withContext(Dispatchers.IO) {
                RealmCartRepository.addToCart(0, product)
            }
        }
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            ProductsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}