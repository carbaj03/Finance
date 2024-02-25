import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import org.acv.hopla.SignIn

@Composable actual fun GoogleSingnIn() {
  val context = LocalContext.current
  val sign = remember { getGoogleSignInClient(context) }

  SignIn(sign)
}

fun getGoogleSignInClient(context: Context): GoogleSignInClient {
  val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
    .requestIdToken("349465254302-g6op9fehu443dtg9cbr5nl9o7bminot1.apps.googleusercontent.com")
    .requestEmail()
    .build()

  return GoogleSignIn.getClient(context, gso)
}

