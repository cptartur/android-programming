package com.example.store.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.findNavController
import com.example.store.BuildConfig
import com.example.store.R
import com.example.store.TokenManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException

class LoginSignupFragment : Fragment() {

    lateinit var signInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.serverClientId)
            .requestEmail()
            .build()
        signInClient = GoogleSignIn.getClient(requireContext(), gso)
        signInClient.signOut()

        val loginButton = view.findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            val action = LoginSignupFragmentDirections.actionLoginSignupFragmentToLocalLoginFragment()
            view.findNavController().navigate(action)
        }

        val registerButton = view.findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            val action = LoginSignupFragmentDirections.actionLoginSignupFragmentToLocalSignupFragment()
            view.findNavController().navigate(action)
        }

        val googleSigninButton = view.findViewById<SignInButton>(R.id.googleButton)
        googleSigninButton.setOnClickListener { startGoogleSignIn() }
    }

    private fun startGoogleSignIn() {
        val signInIntent = signInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)
                TokenManager.addGoogleAccount()
                val activity = LoginSignupFragmentDirections.actionLoginSignupFragmentToBottomNavigationActivity()
                view?.findNavController()?.navigate(activity)
            } catch (e: ApiException) {
                Log.w("Sign in", "Sign in failed" + e.statusCode)
            }
        }
    }

    companion object {
        fun newInstance() =
            LoginSignupFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}