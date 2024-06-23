import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
      userService.login("carbaj03", "password123")
    }
  }
}

@Composable
fun LoginScreen(
  store: LoginStore,
) {
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }

  Surface(
    modifier = Modifier.fillMaxSize().horizontalScroll(rememberScrollState()),
  ){
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
    ) {
      Button(onClick = { store.googleLogin() }) {
        Text("Log in with Google")
      }
      Spacer(modifier = Modifier.size(16.dp))
      Text("or with your email", style = MaterialTheme.typography.labelSmall)
      Spacer(modifier = Modifier.size(16.dp))
      OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Email") },
        placeholder = { Text("name@domain.com") }
      )
      Spacer(modifier = Modifier.size(8.dp))
      OutlinedTextField(
        value = password,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation(),
        onValueChange = { password = it },
        label = { Text("Password") }
      )
      Spacer(modifier = Modifier.size(16.dp))
      Button(onClick = { store.login(email, password) }) {
        Text("Log In")
      }
      Spacer(modifier = Modifier.size(16.dp))
      Text(
        text = "Don't have an account? Create Free Account",
        style = MaterialTheme.typography.labelSmall
      )
    }
  }
}
