package com.example.store.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.realm.models.RealmProduct
import com.example.store.realm.repositories.RealmCartRepository
import com.example.store.realm.repositories.RealmProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

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
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        val productAdapter = MyProductsRecyclerViewAdapter(products as MutableList<RealmProduct>,
            object : OnAddToCartClickListener {
                override fun onAddToCart(product: RealmProduct) {
                    addToCart(product)
                }
            },
            object : OnDetailsClickListener {
                override fun onDetails(product: RealmProduct) {
                    details(product)
                }
            }
        )

        // Set the adapter
        if (recyclerView is RecyclerView) {
            with(recyclerView) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = productAdapter
            }
        }

        setupSearchView(searchView, productAdapter)
    }

    private fun setupSearchView(searchView: SearchView, adapter: MyProductsRecyclerViewAdapter) {
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    p0?.let { adapter.filter(it) }
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    p0?.let { adapter.filter(it) }
                    return true
                }

            }
        )
    }

    private fun addToCart(product: RealmProduct) {
        runBlocking {
            withContext(Dispatchers.IO) {
                RealmCartRepository.addToCart(0, product)
            }
        }
    }

    private fun details(product: RealmProduct) {
        Log.d("Details", "Details clicked.")
        val action =
            ProductsFragmentDirections.actionNavigationProductsToProductDetailsFragment(product.id)
        view?.findNavController()?.navigate(action)
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            ProductsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}