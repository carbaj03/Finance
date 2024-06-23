import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import main.Historical
import main.Portfolio
import main.PortfolioStore
import main.SignalStore
import main.Signals
import main.System

class HomeStore(
  private val themeService: ThemeService
) {
  val state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState(themeService.mode.value))

  fun changeTheme() {
    themeService.toggle()
    state.value = state.value.copy(theme = themeService.mode.value)
  }
}

data class HomeState(
  val theme: Mode
)

@Composable
fun Home(
  homeStore: HomeStore,
  signalStore: SignalStore,
  portfolioStore: PortfolioStore,
) {
  val stocksState = rememberLazyListState()
  val cryptosState = rememberLazyListState()
  val reitState = rememberLazyListState()
  val materialsState = rememberLazyListState()

  var section by rememberSaveable { mutableStateOf(Section.Signals) }
  val state by homeStore.state.collectAsState()

  val allState = rememberLazyListState()
  val buyState = rememberLazyListState()
  val sellState = rememberLazyListState()
  val radarState = rememberLazyListState()

  Scaffold(
    topBar = {
      TopBar(onModeChange = { homeStore.changeTheme() }, mode = state.theme)
    },
    bottomBar = {
      BottomBar(
        selected = section,
        onSelected = { section = it }
      )
    }
  ) { padding ->
    when (section) {
      Section.Signals -> {
        Signals(
          signalStore = signalStore,
          allState = allState,
          buyState = buyState,
          sellState = sellState,
          radarState = radarState,
          modifier = Modifier.padding(padding).fillMaxSize()
        )
      }
      Section.Portfolio -> {
        Portfolio(
          store = portfolioStore,
          stockState = stocksState,
          cryptoState = cryptosState,
          reitState = reitState,
          materialState = materialsState,
          modifier = Modifier.padding(padding).fillMaxSize()
        )
      }
      Section.Historical -> {
        Historical(
          modifier = Modifier.padding(padding).fillMaxSize()
        )
      }
      Section.System -> {
        System(
          modifier = Modifier.padding(padding).fillMaxSize()
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
  mode: Mode,
  onModeChange: () -> Unit
) {
  TopAppBar(
    title = {
      Row(
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
      ) {
        Icon(
          painter = painterResource(R.Images.ic_logo),
          contentDescription = "Home",
          modifier = Modifier.size(32.dp)
        )
        Text("Hopla")
      }
    },
    actions = {
      IconButton(onClick = { onModeChange() }) {
        Icon(
          painter = painterResource(
            when (mode) {
              Mode.Light -> R.Images.DarkMode
              Mode.Dark -> R.Images.LightMode
            }
          ),
          contentDescription = "Search",
          modifier = Modifier.size(24.dp)
        )
      }
    }
  )
}

@Composable
fun BottomBar(
  selected: Section,
  onSelected: (Section) -> Unit,
) {
  HorizontalDivider(
    thickness = 0.5.dp,
    color = MaterialTheme.colorScheme.surfaceVariant
  )
  Row(
    modifier = Modifier.navigationBarsPadding().padding(16.dp)
  ) {
    Section.entries.forEach { Tab ->
      Tab(
        modifier = Modifier.weight(1f),
        selected = selected == Tab,
        onSelected = onSelected
      )
    }
  }
}