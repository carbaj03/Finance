import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.launch

fun main() = application {
  Window(onCloseRequest = ::exitApplication, title = "KotlinProject") {
    App2()
  }
}

@Preview
@Composable
fun AppDesktopPreview() {
  App2()
}

@Composable
fun App2() {
  val notificationService = remember { NotificationService() }
  var text by remember { mutableStateOf("Hello, World!") }
  val scope = rememberCoroutineScope()

  Column {
    OutlinedTextField(value = text, onValueChange = { text = it })
    Button(onClick = {
      scope.launch {
        notificationService.send(SendMessageDto(to = "chat", notification = NotificationBody(title = "hi", body = text)))
      }
    }) {
      Text("Send")
    }

    Button(onClick = {
      scope.launch {
        notificationService.broadcast(SendMessageDto(to = "chat", notification = NotificationBody(title = "hi", body = text)))
      }
    }) {
      Text("Broadcast")
    }
  }
}