package com.example.auth

import com.example.Secrets
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import java.util.*

object GoogleAuth {

    private val transport = NetHttpTransport()
    private val gsonFactory = GsonFactory()
    private val verifier = GoogleIdTokenVerifier.Builder(transport, gsonFactory)
        .setAudience(Collections.singletonList(Secrets.GOOGLE_API_KEY))
        .build()

    fun verifyToken(token: String): GoogleIdToken.Payload? {
        val idToken = verifier.verify(token)
        if (idToken == null) {
            println("Invalid token")
            return null
        }
        return idToken.payload
    }

}