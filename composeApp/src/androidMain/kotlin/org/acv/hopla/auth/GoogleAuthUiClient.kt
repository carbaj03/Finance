package org.acv.hopla.auth

import Logged
import NotLogged
import User
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

fun GoogleSignIn(
    scope2: CoroutineScope,
    context: Context,
) {
    val credentialManager = CredentialManager.create(context)

    val rawNonce = UUID.randomUUID().toString()
    val bytes = rawNonce.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId("WEB_GOOGLE_CLIENT_ID")
        .setNonce(hashedNonce)
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    scope2.launch {
        try {
            val result = credentialManager.getCredential(
                request = request,
                context = context,
            )

            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)

            val googleIdToken = googleIdTokenCredential.idToken

//        supabase.auth.signInWith(IDToken) {
//          idToken = googleIdToken
//          provider = Google
//          nonce = rawNonce
//        }

            // Handle successful sign-in
        } catch (e: GetCredentialException) {
            // Handle GetCredentialException thrown by `credentialManager.getCredential()`
        } catch (e: GoogleIdTokenParsingException) {
            // Handle GoogleIdTokenParsingException thrown by `GoogleIdTokenCredential.createFrom()`
        } catch (e: Exception) {
            // Handle unknown exceptions
        }
    }
}

class GoogleAuthUiClient(
    val context: Context,
    val scope: CoroutineScope,
) {
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(true)
        .setServerClientId("994737436968-vc158aum1ruraklgvckbc5bjeg0pj20d.apps.googleusercontent.com")
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()

    var responseJson: String? = null
    val credentialManager = CredentialManager.create(context)

    suspend fun signIn(): User =
        try {
            val result: GetCredentialResponse = credentialManager.getCredential(
                request = request,
                context = context,
            )
            handleSignIn(result)
        } catch (e: GetCredentialException) {
            handleFailure(e)
            NotLogged
        }

    private fun handleFailure(e: GetCredentialException) {
        val statusCode = e.type
        val statusMessage = e.errorMessage
        Log.e("TAG", "Error status code: $statusCode, status message: $statusMessage")
    }

    private fun handleSignIn(result: GetCredentialResponse): User {
        val credential = result.credential

        return when (credential) {
            is PublicKeyCredential -> {
                responseJson = credential.authenticationResponseJson
                Logged("PublicKeyCredential")
            }

            is PasswordCredential -> {
                val username = credential.id
                val password = credential.password
                Logged(username)
            }

            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        val googleIdToken = googleIdTokenCredential.idToken
                        Logged(googleIdToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("TAG", "Received an invalid google id token response", e)
                        NotLogged
                    }
                } else {
                    Log.e("TAG", "Unexpected type of credential")
                    NotLogged
                }
            }

            else -> {
                Log.e("TAG", "Unexpected type of credential")
                NotLogged
            }
        }
    }
}