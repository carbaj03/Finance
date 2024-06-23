import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.ClearCredentialStateRequest
import org.acv.hopla.auth.GoogleAuthUiClient

//@Composable
//actual fun rememberGoogleTap(
//  userService: UserService
//): GoogleTap {
//  val context = LocalContext.current
//  val scope = rememberCoroutineScope()
//  val googleAuthUiClient = remember {
//    GoogleAuthUiClient(Identity.getSignInClient(context))
//  }
//  val result = rememberDeferrable<Intent?>()
//
//  val launcher = rememberLauncherForActivityResult(
//    contract = ActivityResultContracts.StartIntentSenderForResult(),
//    onResult = {
//      if (it.resultCode == ComponentActivity.RESULT_OK) {
//        result.complete(it.data)
//      }
//    }
//  )
//
//  return remember {
//    object : GoogleTap {
//      override suspend fun logout() {
//        userService.logout()
//        googleAuthUiClient.signOut()
//      }
//
//      override suspend fun login() {
//        val signInIntentSender = googleAuthUiClient.signIn()!!
//        val request = IntentSenderRequest.Builder(signInIntentSender).build()
//        launcher.launch(request)
//
//        result.await()?.let {
//          val signInResult = googleAuthUiClient.signInWithIntent(intent = it)
//          when (signInResult) {
//            is SignInError -> {
//              userService.logout()
//            }
//            is UserData -> {
//              userService.login("1", "1")
//            }
//          }
//        }
//      }
//    }
//  }
//}

@Composable
actual fun rememberGoogleTap(
  userService: UserService
): GoogleTap {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()

  val googleAuthUiClient = remember {
    GoogleAuthUiClient(scope = scope, context = context)
  }

  return remember {
    object : GoogleTap {
      override suspend fun logout() {
        userService.logout()
        googleAuthUiClient.credentialManager.clearCredentialState(ClearCredentialStateRequest())
//        googleAuthUiClient.signOut()
      }

      override suspend fun login() {
        when (val user = googleAuthUiClient.signIn()) {
          is Logged -> userService.login(user.username, "password123")
          NotLogged -> {}
        }
      }
    }
  }
}
