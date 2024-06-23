import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import arrow.optics.optics
import helper.calculateFraction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import main.PortfolioStore
import main.SignalStore


class MainStore(
  private val scope: CoroutineScope,
  private val userService: UserService
) {
  val state: MutableStateFlow<MainState> = MutableStateFlow(MainState(user = NotLogged))

  fun load() {
    scope.launch {
      state.value = state.value.copy(user = userService.user.value)
    }
  }

  fun logout() {
    scope.launch {
      state.value = state.value.copy(user = userService.logout())
    }
  }
}

@optics data class MainState(
   val user: User,
) {
  val userName: String
    get() = when (user) {
      is NotLogged -> "Not logged in"
      is Logged -> user.username
    }

  companion object
}

@Composable
fun MainScreen(
  homeStore: HomeStore,
  store: MainStore,
  portfolioStore: PortfolioStore,
  signalStore: SignalStore,
) {
  val drawerState = rememberDrawerState(DrawerValue.Closed)
  val scope = rememberCoroutineScope()
  val state by store.state.collectAsState()

  LaunchedEffect(Unit) {
    store.load()
  }

  DismissibleNavigationDrawer(
    modifier = Modifier.imePadding(),
    drawerState = drawerState,
    drawerContent = {
      DismissibleDrawerSheet(
        modifier = Modifier.width(360.dp),
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Spacer(modifier = Modifier.height(16.dp))
          Image(
            modifier = Modifier.size(100.dp).clip(RoundedCornerShape(50.dp)) ,
            painter = painterResource(R.Images.ic_user),
            contentDescription = "Avatar",
          )
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = state.userName,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
          )
          Spacer(modifier = Modifier.weight(1f))
          OutlinedButton(
            onClick = {
              store.logout()
              scope.launch { drawerState.close() }
            }
          ) {
            Text("Log out")
          }
          Spacer(modifier = Modifier.height(16.dp))
        }
      }
    }
  ) {

    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Home(
        homeStore = homeStore,
        portfolioStore = portfolioStore,
        signalStore = signalStore,
      )
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