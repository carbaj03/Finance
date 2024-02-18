package main

import CustomTabs
import R
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import domain.Asset
import domain.Asset.Crypto
import domain.Asset.Material
import domain.Asset.Reit
import domain.Asset.Stock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface PortfolioGateway {
  suspend fun stocks(): List<Stock> =
    listOf(
      Stock("AAPL", "05/01/2024", 170f, 160f),
      Stock("GOOGL", "19/01/2024", 2800f, 2750f),
      Stock("MSFT", "28/01/2024", 300f, 290f),
      Stock("AMZN", "26/01/2024", 3200f, 3100f),
      Stock("FB", "25/01/2024", 250f, 240f),
      Stock("TSLA", "03/01/2024", 800f, 780f),
      Stock("NFLX", "09/01/2024", 500f, 485f),
      Stock("NVDA", "04/01/2024", 700f, 690f),
      Stock("INTC", "16/01/2024", 50f, 48f),
      Stock("AMD", "25/01/2024", 100f, 95f),
      Stock("BABA", "15/01/2024", 150f, 145f),
      Stock("SONY", "16/01/2024", 80f, 78f),
      Stock("SNE", "21/01/2024", 85f, 82f),
      Stock("IBM", "13/01/2024", 130f, 128f),
      Stock("HPQ", "26/01/2024", 30f, 28f),
      Stock("XOM", "07/01/2024", 60f, 58f),
      Stock("CVX", "04/01/2024", 120f, 118f),
      Stock("PG", "16/01/2024", 140f, 138f),
      Stock("KO", "01/01/2024", 55f, 54f),
      Stock("PEP", "29/01/2024", 140f, 135f),
      Stock("MCD", "27/01/2024", 230f, 225f),
      Stock("WMT", "13/01/2024", 140f, 138f),
      Stock("DIS", "14/01/2024", 100f, 98f),
      Stock("V", "20/01/2024", 210f, 205f),
      Stock("MA", "25/01/2024", 330f, 325f)
    )

  suspend fun cryptos(): List<Crypto> =
    listOf(
      Crypto("BTC", "2021-01-01", 100f, 90f),
      Crypto("ETH", "2021-01-01", 900f, 890f)
    )

  suspend fun reits(): List<Reit> =
    listOf(
      Reit("O", "2021-01-01", 100f, 90f),
      Reit("STOR", "2021-01-01", 900f, 890f)
    )

  suspend fun materials(): List<Material> =
    listOf(
      Material("X", "2021-01-01", 100f, 90f),
      Material("NUE", "2021-01-01", 900f, 890f)
    )
}

sealed interface Summary {
  val title: String
  val items: List<Asset>
  val performance: String
  val expiration: String
  val dropdown: String
}

data class StockSummary(
  override val title: String = "Stocks",
  override val items: List<Stock>,
  override val performance: String = "9.5%",
  override val expiration: String = "31dias",
  override val dropdown: String = "9.5%"
) : Summary

data class CryptoSummary(
  override val title: String = "Cripto",
  override val items: List<Crypto>,
  override val performance: String = "2.5%",
  override val expiration: String = "15dias",
  override val dropdown: String = "2.5%"
) : Summary

data class ReitSummary(
  override val title: String = "Reit",
  override val items: List<Reit>,
  override val performance: String = "1.5%",
  override val expiration: String = "45dias",
  override val dropdown: String = "1.5%"
) : Summary

data class MaterialSummary(
  override val title: String = "Materials",
  override val items: List<Material>,
  override val performance: String = "4.5%",
  override val expiration: String = "60dias",
  override val dropdown: String = "4.5%"
) : Summary

data class PortfolioState(
  val stockSummary: StockSummary,
  val cryptoSummary: CryptoSummary,
  val reitSummary: ReitSummary,
  val materialSummary: MaterialSummary,
  val selected: AssetType = AssetType.Stock
)

class PortfolioStore(
  private val gateway: PortfolioGateway,
  initialState: PortfolioState = PortfolioState(
    stockSummary = StockSummary(items = emptyList()),
    cryptoSummary = CryptoSummary(items = emptyList()),
    reitSummary = ReitSummary(items = emptyList()),
    materialSummary = MaterialSummary(items = emptyList()),
  )
) {
  private val _state: MutableStateFlow<PortfolioState> = MutableStateFlow(initialState)

  val state: StateFlow<PortfolioState> = _state

  suspend fun onSelected(assetType: Int) {
    _state.value = _state.value.copy(selected = AssetType.entries[assetType])
    _state.value = when(assetType) {
      0 -> _state.value.copy(stockSummary = _state.value.stockSummary.copy(items = gateway.stocks()))
      1 -> _state.value.copy(cryptoSummary = _state.value.cryptoSummary.copy(items = gateway.cryptos()))
      2 -> _state.value.copy(reitSummary = _state.value.reitSummary.copy(items = gateway.reits()))
      3 -> _state.value.copy(materialSummary = _state.value.materialSummary.copy(items = gateway.materials()))
      else -> _state.value
    }
  }

}

@Composable
fun Portfolio(
  store: PortfolioStore,
  stockState: LazyListState,
  cryptoState: LazyListState,
  reitState: LazyListState,
  materialState: LazyListState,
  modifier: Modifier = Modifier
) {

  val state by store.state.collectAsState()
  val scope = rememberCoroutineScope()

  LaunchedEffect(Unit) {
    store.onSelected(0)
  }

  val tabs by remember {
    mutableStateOf(
      listOf(R.Images.ic_stocks, R.Images.ic_cripto, R.Images.ic_reit, R.Images.ic_materials)
    )
  }

  Column(
    modifier = modifier
  ) {
    CustomTabs(
      selected = AssetType.entries.indexOf(state.selected),
      onSelected = { scope.launch { store.onSelected(it) } },
      tabs = tabs
    )

    when (state.selected) {
      AssetType.Stock -> {
        state.stockSummary(stockState)
      }
      AssetType.Crypto -> {
        state.cryptoSummary(cryptoState)
      }
      AssetType.Reit -> {
        state.reitSummary(reitState)
      }
      AssetType.Material -> {
        state.materialSummary(materialState)
      }
    }
  }
}

@Composable
operator fun Summary.invoke(
  state: LazyListState,
) {
  LazyColumn(
    state = state,
  ) {
    item {
      Column(
        modifier = Modifier.padding(16.dp)
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          header(
            label = R.Strings.performance,
            value = performance,
            modifier = Modifier.weight(1f),
            contentColor = R.Color.VintageGreen
          )
          header(
            label = R.Strings.expiration,
            value = expiration,
            modifier = Modifier.weight(1f),
            contentColor = Color.Gray
          )
          header(
            label = R.Strings.dropdown,
            value = dropdown,
            modifier = Modifier.weight(1f),
            contentColor = R.Color.VintageRed
          )
        }
      }
    }
    header()
    items(items) { Asset ->
      Asset()
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.header(
  modifier: Modifier = Modifier,
) {
  stickyHeader {
    Row(
      modifier = modifier
        .background(color = MaterialTheme.colorScheme.primaryContainer)
        .fillMaxWidth()
        .padding(8.dp, 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      TextLabel(
        text = "Ticker",
        modifier = Modifier.weight(1f),
      )
      TextLabel(
        text = "Añadida",
        modifier = Modifier.weight(1f)
      )
      TextLabel(
        text = "Entrada",
        modifier = Modifier.weight(1f)
      )
      TextLabel(
        text = "Stop",
        modifier = Modifier.weight(1f)
      )
    }
  }
}

@Composable
fun TextLabel(
  text: String,
  modifier: Modifier = Modifier
) {
  Text(
    modifier = modifier,
    text = text,
    style = MaterialTheme.typography.labelSmall,
    color = MaterialTheme.colorScheme.onPrimaryContainer
  )
}

@Composable
private operator fun Asset.invoke(
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxWidth().padding(8.dp, 8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = ticker,
      style = MaterialTheme.typography.bodyLarge,
      modifier = Modifier.weight(1f)
    )
    Text(
      text = added,
      style = MaterialTheme.typography.labelSmall,
      modifier = Modifier.weight(1f)
    )
    Text(
      text = entry.toString(),
      style = MaterialTheme.typography.labelSmall,
      modifier = Modifier.weight(1f)
    )
    Text(
      text = stop.toString(),
      style = MaterialTheme.typography.labelSmall,
      modifier = Modifier.weight(1f)
    )
  }
}

@Composable
private fun header(
  label: String,
  value: String,
  contentColor: Color = MaterialTheme.colorScheme.onSurface,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      color = Color.Gray.copy(alpha = 0.8f)
    )
    Text(
      text = value,
      style = MaterialTheme.typography.titleLarge,
      color = contentColor,
    )
  }
}
