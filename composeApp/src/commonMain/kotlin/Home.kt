import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import main.Historical
import main.Portfolio
import main.PortfolioStore
import main.SignalStore
import main.Signals
import main.System

@Composable
fun Home(dependencies: Dependencies) {
  val portfolioStore = remember { PortfolioStore(portfolioRepository = PortfolioGatewayImpl(dependencies.userService)) }
  val signalStore = remember { SignalStore(signalRepository = SignalRepositoryImpl(), newsRepository = NewsRepositoryImpl(dependencies.userService)) }

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
    topBar = {
      TopBar()
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