import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration.Companion.seconds

@Composable
fun App(
  dependencies: Dependencies
) {
  MaterialTheme(
    colorScheme = MaterialTheme.colorScheme.copy(
      primary = Color.Black,
      onPrimary = Color.White,
      onPrimaryContainer = Color.White,
      primaryContainer = Color.Black,
      surfaceVariant = R.Color.VintageGreen,
      onSurfaceVariant = Color.Black,
      secondary = Color.Black,
      onSecondary = Color.White,
      secondaryContainer = Color.Black,
      onSecondaryContainer = Color.White,
    )
  ) {

    var splah by remember { mutableStateOf(true) }

    val user: User by dependencies.userService.user.collectAsState()

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
      delay(2.seconds)
      splah = false
    }

    val store = remember { LoginStore(scope = scope, googleTap = dependencies.googleTap, userService = dependencies.userService) }
    val mainStore = remember { MainStore(scope = scope, userService = dependencies.userService) }

    when {
      splah -> SplashScreen()
      else -> when (user) {
        is NotLogged -> LoginScreen(store = store)
        is Logged -> MainScreen(store = mainStore, dependencies = dependencies)
      }
    }
  }
}

@Composable
fun CustomTabs(
  selected: Int,
  onSelected: (Int) -> Unit,
  tabs: List<String>,
) {
  ScrollableTabRow(
    selectedTabIndex = selected,
    modifier = Modifier.fillMaxWidth(),
    edgePadding = 0.dp,
    divider = { HorizontalDivider(thickness = 0.5.dp) },
    indicator = { tabPositions ->
      SecondaryIndicator(
        modifier = Modifier.customTabIndicatorOffset(
          currentTabPosition = tabPositions[selected],
          tabWidth = 40.dp
        ),
      )
    }
  ) {
    tabs.forEachIndexed { index, icon ->
      MyTab(
        selected = index == selected,
        onSelected = { onSelected(index) },
        icon = icon
      )
    }
  }
}

@Composable
fun MyTab(
  icon: String,
  selected: Boolean,
  onSelected: () -> Unit
) {
  Box(
    modifier = Modifier.size(48.dp).indication(
      interactionSource = MutableInteractionSource(),
      indication = null
    ).clickable { onSelected() },
    contentAlignment = Alignment.Center
  ) {
    Icon(
      painter = painterResource(icon),
      contentDescription = "Favorite",
      tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
    )
  }
}

enum class Section(
  val title: String,
  val icon: String,
  val iconDisabled: String,
) {
  Signals(R.Strings.signals, R.Images.ic_alerts, R.Images.ic_alerts_disabled),
  Portfolio(R.Strings.portfolio, R.Images.ic_portfolio, R.Images.ic_portfolio_disabled),
  Historical(R.Strings.historical, R.Images.ic_historical, R.Images.ic_historical_disabled),
  System(R.Strings.system, R.Images.ic_system, R.Images.ic_system_disabled),
}

@Composable
operator fun Section.invoke(
  modifier: Modifier,
  selected: Boolean,
  onSelected: (Section) -> Unit,
) {
  Tab(
    modifier = modifier,
    icon = if (selected) icon else iconDisabled,
    text = title,
    selected = selected,
    onSelected = { onSelected(this) }
  )
}

@Composable
fun Tab(
  text: String,
  icon: String,
  onSelected: () -> Unit,
  selected: Boolean,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.clickable(indication = null, interactionSource = MutableInteractionSource()) { onSelected() },
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Icon(
      painter = painterResource(icon),
      contentDescription = null,
      tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
    )
    Text(
      text = text,
      style = MaterialTheme.typography.labelSmall,
      color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
    )
  }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun painterResource(icon: String) = painterResource(DrawableResource(icon))