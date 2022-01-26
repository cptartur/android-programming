package com.example.store.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.store.R
import com.example.store.databinding.FragmentLocalSignupBinding
import com.example.store.models.User
import com.example.store.models.UserType
import com.example.store.realm.repositories.RealmUserRepository
import com.example.store.repositories.UserRepository
import com.example.store.services.UserService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LocalSignupFragment : Fragment() {

    private lateinit var binding: FragmentLocalSignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_local_signup, container, false)
        binding = FragmentLocalSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val registerButton = binding.buttonRegister
        registerButton.setOnClickListener { register() }
    }

    private fun register() {
        val name = binding.registerName.editText?.text.toString()
        val email = binding.registerEmail.editText?.text.toString()
        val password = binding.registerPassword.editText?.text.toString()
        lifecycleScope.launch(Dispatchers.IO) {
            val user = User(name = name, id = 0, email = email, password = password, type = UserType.LOCAL)
            try {
                UserRepository.createUser(user)
                activity?.runOnUiThread {
                    val action = LocalSignupFragmentDirections.actionLocalSignupFragmentToLocalLoginFragment()
                    binding.root.findNavController().navigate(action)
                }
            } catch (ex: HttpException) {
                showHttpError(ex.code(), ex.message())
            }
        }
    }

    private fun showHttpError(code: Int, message: String) {
        activity?.runOnUiThread() {
            Log.d("HttpException", "Server Error: ${code}: $message")

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Sign up failed")
                .setMessage("Failed to create an account")
                .setPositiveButton("Try again") { _, _ -> register() }
                .setNegativeButton("Cancel") { _, _ ->
                    binding.registerName.editText?.setText("")
                    binding.registerEmail.editText?.setText("")
                    binding.registerPassword.editText?.setText("")
                }
                .show()
        }
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LocalSignupFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}