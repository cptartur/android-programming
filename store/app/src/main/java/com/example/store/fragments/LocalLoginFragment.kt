package com.example.store.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.store.R
import com.example.store.TokenManager
import com.example.store.databinding.FragmentLocalLoginBinding
import com.example.store.repositories.AuthRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class LocalLoginFragment : Fragment() {
    private lateinit var binding: FragmentLocalLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_local_login, container, false)
        binding = FragmentLocalLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton = view.findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener { login() }
    }

    private fun login() {
        val email = binding.loginEmail.editText?.text.toString()
        val password = binding.loginPassword.editText?.text.toString()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                AuthRepository.localLogin(email, password)
                TokenManager.addLocalAccount(requireContext(), email, password)
                activity?.runOnUiThread {
                    val action = LocalLoginFragmentDirections.actionLocalLoginFragmentToBottomNavigationActivity()
                    binding.root.findNavController().navigate(action)
                }
            } catch (ex: HttpException) {
                showHttpError(ex.code(), ex.message())
            }
        }
        binding.loginEmail.editText?.setText("")
        binding.loginPassword.editText?.setText("")
    }

    private fun showHttpError(code: Int, message: String) {
        activity?.runOnUiThread() {
            Log.d("HttpException", "Server Error: ${code}: $message")

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Sign in failed")
                .setMessage("Failed to sign in. Incorrect login or password")
                .setNeutralButton("Try again") { _, _ -> }
                .show()
        }
    }
}
