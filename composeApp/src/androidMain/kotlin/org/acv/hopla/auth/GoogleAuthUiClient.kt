package org.acv.hopla.auth

import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await


class GoogleAuthUiClient(
  private val oneTapClient: SignInClient
) {
  private val auth = Firebase.auth

  suspend fun signIn(): IntentSender? =
    try {
      oneTapClient.beginSignIn(buildSignInRequest()).await()
    } catch (e: Exception) {
      e.printStackTrace()
      if (e is CancellationException) throw e
      null
    }?.pendingIntent?.intentSender

  suspend fun signInWithIntent(
    intent: Intent
  ): SignInResult {
    val credential = oneTapClient.getSignInCredentialFromIntent(intent)
    val googleIdToken = credential.googleIdToken
    val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
    return try {
//      val user: FirebaseUser? = auth.signInWithCredential(googleCredentials).await().user
//      user?.let {
//        UserData(
//          userId = it.uid,
//          username = it.displayName,
//          profilePictureUrl = it.photoUrl?.toString()
//        )
//      } ?:  SignInError("User is null")
      UserData(
        userId = "123",
        username = "username",
        profilePictureUrl = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_92x30dp.png"
      )
    } catch (e: Exception) {
      e.printStackTrace()
      if (e is CancellationException) throw e
      SignInError(e.message!!)
    }
  }

  suspend fun signOut() {
    try {
      oneTapClient.signOut().await()
      auth.signOut()
    } catch (e: Exception) {
      e.printStackTrace()
      if (e is CancellationException) throw e
    }
  }

  fun getSignedInUser(): UserData? = auth.currentUser?.run {
    UserData(
      userId = uid,
      username = displayName,
      profilePictureUrl = photoUrl?.toString()
    )
  }

  private fun buildSignInRequest(): BeginSignInRequest =
    BeginSignInRequest.Builder()
      .setGoogleIdTokenRequestOptions(
        GoogleIdTokenRequestOptions.builder()
          .setSupported(true)
          .setFilterByAuthorizedAccounts(false)
          .setServerClientId("994737436968-vc158aum1ruraklgvckbc5bjeg0pj20d.apps.googleusercontent.com")
          .build()
      )
      .setAutoSelectEnabled(true)
      .build()
}