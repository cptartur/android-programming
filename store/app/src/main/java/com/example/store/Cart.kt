package com.example.store

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.store.databinding.ActivityCartBinding
import com.example.store.realm.repositories.RealmCartRepository
import com.example.store.repositories.PaymentRepository
import com.example.store.models.PaymentTotal
import com.example.store.repositories.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class Cart : AppCompatActivity() {
    private lateinit var paymentSheet: PaymentSheet
    private lateinit var paymentIntentClientSecret: String
    private lateinit var binding: ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart)

        val payButton = binding.buttonPay
        payButton.setOnClickListener {
            onPayClicked(it)
        }

        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    private suspend fun fetchPaymentIntent(total: Long, token: String) {
        paymentIntentClientSecret = PaymentRepository.createPaymentIntent(PaymentTotal(total * 100), token).clientSecret
    }

    private fun onPayClicked(view: View) {
        val configuration = PaymentSheet.Configuration("Store")
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.VISIBLE
            }
            val cart = RealmCartRepository.getCart(0)
            var total = cart?.products?.sumOf { it.price.toLong() } ?: 0
            if (total <= 0) {
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                }
                return@launch
            }
            try {
                val account = GoogleSignIn.getLastSignedInAccount(this@Cart)!!
                val idToken = account.idToken!!
                val token = AuthRepository.login(idToken).token
                Log.d("cart token", token)
                AuthRepository.testLogin(token)

                fetchPaymentIntent(total, "1234")
                paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, configuration)
            } catch (ex: HttpException) {
                showHttpError(ex.code(), ex.message())
            }
        }
    }

    private fun showHttpError(code: Int, message: String) {
        runOnUiThread {
            Log.d("HttpException", "Server Error: ${code}: $message")
            binding.progressBar.visibility = View.GONE

            MaterialAlertDialogBuilder(this)
                .setTitle("Server error")
                .setMessage("An error occurred trying to process your payment request\n Server Error: ${code}: $message")
                .setNegativeButton("Accept") { _, _ -> true }
                .setPositiveButton("Retry") { _, _ -> onPayClicked(View(this)) }
                .show()
        }
    }

    private fun onPaymentSheetResult(paymentResult: PaymentSheetResult) {
        when (paymentResult) {
            is PaymentSheetResult.Completed -> {
                showToast("Payment successful")
                binding.buttonPay.visibility = View.GONE
                runBlocking(Dispatchers.IO) {
                    RealmCartRepository.removeCart(0)
                    RealmCartRepository.createCart(0)
                }
                finish()
            }
            is PaymentSheetResult.Canceled -> {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}