import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import helper.customTabIndicatorOffset
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import main.PortfolioStore
import main.SignalStore
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration.Companion.seconds

@Composable
expect fun rememberGoogleTap(userService: UserService): GoogleTap

@Composable
fun App(
  onThemeChange: (Mode) -> Unit,
) {
  val userService = remember {
    object : UserService {
      private val _user: MutableStateFlow<User> = MutableStateFlow(NotLogged)

      override val user: StateFlow<User> = _user

      override suspend fun login(username: String, password: String): Logged {
        _user.value = Logged(username)
        return Logged(username)
      }

      override suspend fun logout(): NotLogged {
        _user.value = NotLogged
        return NotLogged
      }
    }
  }

  val googleTap = rememberGoogleTap(userService)
  val themeService = remember {
    object : ThemeService {
      private val _mode: MutableStateFlow<Mode> = MutableStateFlow(Mode.Light)
      override val mode: StateFlow<Mode> = _mode
      override fun toggle() {
        _mode.value = when (_mode.value) {
          Mode.Light -> Mode.Dark
          Mode.Dark -> Mode.Light
        }
        onThemeChange(_mode.value)
      }
    }
  }

  val dependencies = remember {
    object : Dependencies {
      override val googleTap: GoogleTap = googleTap
      override val userService: UserService = userService
      override val themeService: ThemeService = themeService
    }
  }

  val scope = rememberCoroutineScope()
  val mainStore = remember { MainStore(scope = scope, userService = dependencies.userService) }

  val mode by dependencies.themeService.mode.collectAsState()

  Theme(mode = mode) {
    var splah by remember { mutableStateOf(true) }
    val user: User by dependencies.userService.user.collectAsState()

    LaunchedEffect(Unit) {
      delay(2.seconds)
      splah = false
    }

    when {
      splah -> SplashScreen()
      else -> when (user) {
        is NotLogged -> {
          val store = remember { LoginStore(scope = scope, googleTap = dependencies.googleTap, userService = dependencies.userService) }
          LoginScreen(store = store)
        }
        is Logged -> {
          val portfolioStore = remember { PortfolioStore(portfolioRepository = PortfolioGatewayImpl(dependencies.userService)) }
          val signalStore = remember { SignalStore(signalRepository = SignalRepositoryImpl(), newsRepository = NewsRepository(dependencies.userService)) }
          val homeStore = remember { HomeStore(themeService = dependencies.themeService) }

          MainScreen(
            homeStore = homeStore,
            store = mainStore,
            portfolioStore = portfolioStore,
            signalStore = signalStore,
          )
        }
      }
    }
  }
}

@Composable
fun CustomTabs(
  selected: Int,
  onSelected: (Int) -> Unit,
  tabs: List<String>,
  modifier: Modifier = Modifier
) {
  ScrollableTabRow(
    selectedTabIndex = selected,
    modifier = modifier,
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