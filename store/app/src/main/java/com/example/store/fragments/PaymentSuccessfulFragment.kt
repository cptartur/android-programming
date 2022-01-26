package com.example.store.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.store.R
import com.example.store.databinding.FragmentPaymentSuccessfulBinding

class PaymentSuccessfulFragment : Fragment() {

    private lateinit var binding: FragmentPaymentSuccessfulBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_payment_successful, container, false)
        binding = FragmentPaymentSuccessfulBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonBackToCart = binding.buttonBackToCart
        buttonBackToCart.setOnClickListener {
            val action = PaymentSuccessfulFragmentDirections.actionPaymentSuccessfulFragmentToNavigationCart()
            view.findNavController().navigate(action)
        }
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            PaymentSuccessfulFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}