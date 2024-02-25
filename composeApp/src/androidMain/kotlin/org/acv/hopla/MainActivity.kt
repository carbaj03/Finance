package org.acv.hopla

import App
import Dependencies
import GoogleTap
import Logged
import MockDependencies
import NotLogged
import User
import UserService
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.acv.hopla.auth.GoogleAuthUiClient
import org.acv.hopla.auth.SignInError
import org.acv.hopla.auth.UserData
import rememberDeferrable

@Composable
fun Tab(
  googleAuthUiClient: GoogleAuthUiClient,
  userService: UserService
): GoogleTap {
  val result = rememberDeferrable<Intent?>()

  val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartIntentSenderForResult(),
    onResult = {
      if (it.resultCode == ComponentActivity.RESULT_OK) {
        result.complete(it.data)
      }
    }
  )

  return remember {
    object : GoogleTap {
      override suspend fun logout() {
        googleAuthUiClient.signOut()
      }

      override suspend fun login() {
        val signInIntentSender = googleAuthUiClient.signIn()!!
        val request = IntentSenderRequest.Builder(signInIntentSender).build()
        launcher.launch(request)

        result.await()?.let {
          val signInResult = googleAuthUiClient.signInWithIntent(intent = it)
          when (signInResult) {
            is SignInError -> {
              userService.logout()
            }
            is UserData -> {
              userService.login("1", "1")
            }
          }
        }
      }
    }
  }
}

class MainActivity : ComponentActivity() {

  private val googleAuthUiClient: GoogleAuthUiClient by lazy {
    GoogleAuthUiClient(
      oneTapClient = Identity.getSignInClient(applicationContext)
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
      navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
    )

    super.onCreate(savedInstanceState)

    setContent {

      val userService = remember {
        object : UserService {
          private val _user: MutableStateFlow<User> = MutableStateFlow(NotLogged)

          override val user: StateFlow<User> = _user

          override suspend fun login(username: String, password: String): Logged {
            _user.value = Logged("")
            return Logged("")
          }

          override suspend fun logout(): NotLogged {
            _user.value = NotLogged
            return NotLogged
          }
        }
      }

      val tab = Tab(googleAuthUiClient, userService)

      val dependencies = remember {
        object : Dependencies {
          override val googleTap: GoogleTap = tab
          override val userService: UserService = userService
        }
      }

      App(dependencies)
    }
  }
}

@Preview
@Composable
fun AppAndroidPreview() {
  App(MockDependencies())
}