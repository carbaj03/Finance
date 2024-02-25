import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainStore(
  private val scope: CoroutineScope,
  private val userService: UserService
) {
  val user: MutableStateFlow<User> = MutableStateFlow(NotLogged)

  fun logout() {
    scope.launch {
      user.value = userService.logout()
    }
  }
}

@Composable
fun MainScreen(
  store: MainStore,
  dependencies: Dependencies
) {
  val drawerState = rememberDrawerState(DrawerValue.Closed)
  val scope = rememberCoroutineScope()

  DismissibleNavigationDrawer(
    modifier = Modifier.imePadding(),
    drawerState = drawerState,
    drawerContent = {
      DismissibleDrawerSheet(
        modifier = Modifier.width(360.dp),
      ) {
        Column {
          Text("Hello, ${store.user}!")
          Button(
            onClick = {
              store.logout()
              scope.launch { drawerState.close() }
            }
          ) {
            Text("Close drawer")
          }
        }
      }
    }
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Home(dependencies = dependencies)
    }

    val minValue = -with(LocalDensity.current) { 360.0.dp.toPx() }
    val maxValue = 0f

    Canvas(
      modifier = Modifier.fillMaxSize()
    ) {
      drawRect(
        color = Color.Black.copy(alpha = 0.5f),
        alpha = calculateFraction(minValue, maxValue, drawerState.currentOffset)
      )
    }
  }
}