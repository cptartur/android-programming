package com.example.store.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.store.R
import com.example.store.fragments.placeholder.PlaceholderContent
import com.example.store.realm.RealmConfig
import com.example.store.realm.models.RealmCart
import com.example.store.realm.models.RealmProduct
import com.example.store.realm.repositories.RealmCartRepository
import io.realm.Realm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.FieldPosition

class CartFragment : Fragment() {

    private var columnCount = 1
    private lateinit var cart: RealmCart

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
        val view = inflater.inflate(R.layout.fragment_cart_list, container, false)

        runBlocking {
            withContext(Dispatchers.IO) {
                cart = RealmCartRepository.getCart(0) ?: RealmCart()
            }
        }

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyCartRecyclerViewAdapter(cart.products ?: emptyList(),
                object : OnRemoveItemListener {
                    override fun onRemoveItem(item: RealmProduct, position: Int) {
                        removeItem(item, position)
                        adapter!!.notifyItemRemoved(position)
                    }
                })
            }
        }
        return view
    }

    private fun removeItem(item: RealmProduct, position: Int) {
        runBlocking {
            withContext(Dispatchers.IO) {
                RealmCartRepository.removeFromCart(0, item)
                cart.products?.removeAt(position)
            }
        }
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}