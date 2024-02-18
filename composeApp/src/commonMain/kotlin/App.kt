import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import main.Historical
import main.Portfolio
import main.PortfolioGateway
import main.PortfolioStore
import main.SignalRepositoryImpl
import main.SignalStore
import main.Signals
import main.System
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun App() {
  MaterialTheme(
    colorScheme = MaterialTheme.colorScheme.copy(
      primary = Color.Black,
      onPrimary = Color.White,
      onPrimaryContainer = Color.White,
      primaryContainer = Color.Black,
      surfaceVariant = R.Color.VintageGreen,
      onSurfaceVariant = Color.Black,
      onSecondary = Color.Black,
      secondary = R.Color.VintageGreen,
      secondaryContainer = Color.Black,
      onSecondaryContainer = Color.White,
    )

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
            Text("Hello, World!")
          }
        }
      }
    ) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Home()
      }

      val minValue = -with(LocalDensity.current) { 360.0.dp.toPx() }
      val maxValue = 0f

      Canvas(
        modifier = Modifier.fillMaxSize()
      ) {
        drawRect(
          color = Color.Black.copy(alpha = 0.5f),
          alpha = calculateFraction(minValue, maxValue, drawerState.offset.value)
        )
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
    divider = { Divider(thickness = 0.5.dp) },
    indicator = { tabPositions ->
      TabRowDefaults.Indicator(
        modifier = Modifier.customTabIndicatorOffset(
          currentTabPosition = tabPositions[selected],
          tabWidth = 40.dp
        )
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

@OptIn(ExperimentalResourceApi::class)
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
) {
  Signals(R.Strings.signals, R.Images.ic_alerts),
  Portfolio(R.Strings.portfolio, R.Images.ic_portfolio),
  Historical(R.Strings.historical, R.Images.ic_historical),
  System(R.Strings.system, R.Images.ic_system)
}

@Composable
fun Home() {
  val portfolioStore = remember { PortfolioStore(gateway = object : PortfolioGateway {}) }
  val signalStore = remember { SignalStore(signalRepository = SignalRepositoryImpl()) }

  val stocksState = rememberLazyListState()
  val cryptosState = rememberLazyListState()
  val reitState = rememberLazyListState()
  val materialsState = rememberLazyListState()

  var section by rememberSaveable { mutableStateOf(Section.Signals) }

  val allState = rememberLazyListState()
  val buyState = rememberLazyListState()
  val sellState = rememberLazyListState()
  val radarState = rememberLazyListState()

  Scaffold(
    topBar = { TopBar() },
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
fun TopBar() {
  TopAppBar(
    title = { Text("Hopla") },
  )
}

@Composable
fun BottomBar(
  selected: Section,
  onSelected: (Section) -> Unit,
) {
  Divider(
    thickness = 0.5.dp,
    color = MaterialTheme.colorScheme.surfaceVariant,
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

@Composable
operator fun Section.invoke(
  modifier: Modifier,
  selected: Boolean,
  onSelected: (Section) -> Unit,
){
  Tab(
    modifier = modifier,
    icon = icon,
    text = title,
    selected = selected,
    onSelected = { onSelected(this) }
  )
}

@OptIn(ExperimentalResourceApi::class)
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
      tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
    )
    Text(
      text = text,
      style = MaterialTheme.typography.labelSmall,
      color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
    )
  }
}