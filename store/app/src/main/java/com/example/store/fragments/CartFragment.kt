package com.example.store.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.store.R
import com.example.store.TokenManager
import com.example.store.databinding.FragmentCartListBinding
import com.example.store.models.PaymentIntent
import com.example.store.models.PaymentTotal
import com.example.store.realm.models.RealmCart
import com.example.store.realm.models.RealmProduct
import com.example.store.realm.repositories.RealmCartRepository
import com.example.store.repositories.AuthRepository
import com.example.store.repositories.PaymentRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class CartFragment : Fragment() {

    private var columnCount = 1
    private lateinit var cart: RealmCart

    private lateinit var paymentSheet: PaymentSheet
    private lateinit var paymentIntent: PaymentIntent
    private lateinit var binding: FragmentCartListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartListBinding.inflate(inflater, container, false)
//        val view = inflater.inflate(R.layout.fragment_cart_list, container, false)
        val view = binding.root

        runBlocking {
            withContext(Dispatchers.IO) {
                cart = RealmCartRepository.getCart(0) ?: kotlin.run {
                    RealmCartRepository.createCart(0)
                    RealmCartRepository.getCart(0)!!
                }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.list)
        val payButton = view.findViewById<Button>(R.id.buttonPay)

        // Set the adapter
        if (recyclerView is RecyclerView) {
            with(recyclerView) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                val adapter = MyCartRecyclerViewAdapter(cart.products ?: emptyList(),
                    object : OnRemoveItemListener {
                        override fun onRemoveItem(item: RealmProduct, position: Int) {
                            removeItem(item, position)
                            adapter!!.notifyItemRemoved(position)
                        }
                    })

                adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {

                    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                        super.onItemRangeRemoved(positionStart, itemCount)
                        if (adapter.itemCount == 0) {
                            payButton.visibility = View.GONE
                        } else {
                            payButton.visibility = View.VISIBLE
                        }
                    }

                })
                this.adapter = adapter
            }
        }

        if (recyclerView.adapter!!.itemCount != 0) {
            payButton.visibility = View.VISIBLE
        }

        payButton.setOnClickListener {
            onPayClicked()
        }

        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
    }

    private fun removeItem(item: RealmProduct, position: Int) {
        runBlocking {
            withContext(Dispatchers.IO) {
                RealmCartRepository.removeFromCart(0, item)
                cart.products?.removeAt(position)
            }
        }
    }

    private fun showToast(message: String) {
        activity?.runOnUiThread() {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
    }

    private suspend fun fetchPaymentIntent(total: Long, token: String) {
        paymentIntent = PaymentRepository.createPaymentIntent(PaymentTotal(total * 100), token)
    }

    private fun onPayClicked() {
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.VISIBLE
            }
            val cart = RealmCartRepository.getCart(0)
            val total = cart?.products?.sumOf { it.price.toLong() } ?: 0
            if (total <= 0) {
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                }
                return@launch
            }
            try {
                val token = TokenManager.getAuthToken(requireContext()) ?: kotlin.run {
                    showHttpError(1,"Failed to get auth token")
                    return@launch
                }

                Log.d("Payment", "Got auth token $token")

                AuthRepository.testLogin(token)
                Log.d("Payment", "Tested login")

                fetchPaymentIntent(total, token)
                Log.d("Payment", "Fetched payment intent")

                val paymentIntentClientSecret = paymentIntent.clientSecret
                val customerConfig = PaymentSheet.CustomerConfiguration(
                    paymentIntent.customer,
                    paymentIntent.ephemeralKey
                )
                val publishableKey = paymentIntent.publishableKey
                PaymentConfiguration.init(requireContext(), publishableKey)

                paymentSheet.presentWithPaymentIntent(
                    paymentIntentClientSecret,
                    PaymentSheet.Configuration(
                        merchantDisplayName = "Store",
                        customer = customerConfig,
                    )
                )
            } catch (ex: HttpException) {
                Log.d("HttpException", ex.message())
                showHttpError(ex.code(), ex.message())
            }
        }
    }

    private fun showHttpError(code: Int, message: String) {
        activity?.runOnUiThread() {
            Log.d("HttpException", "Server Error: ${code}: $message")
            binding.progressBar.visibility = View.GONE

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Server error")
                .setMessage("An error occurred trying to process your payment request\n Server Error: ${code}: $message")
                .setNegativeButton("Accept") { _, _ -> }
                .setPositiveButton("Retry") { _, _ -> onPayClicked() }
                .show()
        }
    }

    private fun onPaymentSheetResult(paymentResult: PaymentSheetResult) {
        when (paymentResult) {
            is PaymentSheetResult.Completed -> {
                showToast("Payment successful")
//                binding.buttonPay.visibility = View.GONE
//                binding.progressBar.visibility = View.GONE
//                binding.list.visibility = View.GONE
                runBlocking(Dispatchers.IO) {
                    RealmCartRepository.removeCart(0)
                    RealmCartRepository.createCart(0)
                }
                val action = CartFragmentDirections.actionNavigationCartToPaymentSuccessfulFragment()
                binding.root.findNavController().navigate(action)
            }
            is PaymentSheetResult.Canceled -> {
                showToast("Payment cancelled")
            }
            is PaymentSheetResult.Failed -> {
                showHttpError(1, "Payment failed: ${paymentResult.error}")
            }
        }
        binding.buttonPay.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.list.visibility = View.GONE
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}