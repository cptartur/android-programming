package com.example.store.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.store.databinding.FragmentProductDetailsBinding
import com.example.store.repositories.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking


class ProductDetailsFragment : Fragment() {
    private val args: ProductDetailsFragmentArgs by navArgs()
    private var productId: Int? = null

    private lateinit var binding: FragmentProductDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productId = args.productId
        Log.d("Details", "Product id is $productId")

        val product = productId?.let {
            runBlocking(Dispatchers.IO) {
                ProductRepository.getProduct(it)
            }
        }
        binding.productDetailsTitle.text = product?.name ?: "Could not fetch product title."
        binding.productDetailsText.text =
            product?.description ?: "Could not fetch product description."

        val imageView = binding.imageView
        imageView.load(product?.imageUrl)
    }
}