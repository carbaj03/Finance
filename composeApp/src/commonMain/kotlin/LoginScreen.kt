import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LoginStore(
  private val scope: CoroutineScope,
  private val googleTap: GoogleTap,
  private val userService: UserService
) {

   fun googleLogin() {
    scope.launch {
      googleTap.login()
    }
  }

   fun login(username: String, password: String) {
    scope.launch {
      userService.login(username, password)
    }
  }
}

@Composable
fun LoginScreen(
  store: LoginStore,
) {
  var username by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }

  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    OutlinedTextField(
      value = username,
      onValueChange = { username = it },
      label = { Text("Username") }
    )
    OutlinedTextField(
      value = password,
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
      onValueChange = { password = it },
      label = { Text("Password") }
    )
    Spacer(modifier = Modifier.size(16.dp))
    Button(onClick = { store.login(username, password) }) {
      Text("Login")
    }
    Button(onClick = { store.googleLogin() }) {
      Text("Sign in with Google")
    }
//    GoogleSingnIn()
  }
}

@Composable
expect fun GoogleSingnIn()