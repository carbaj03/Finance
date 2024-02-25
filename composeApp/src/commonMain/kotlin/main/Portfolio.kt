package main

import Asset
import AssetType
import CryptoSummary
import CustomTabs
import MaterialSummary
import PortfolioGateway
import R
import ReitSummary
import StockSummary
import Summary
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PortfolioState(
  val stockSummary: StockSummary,
  val cryptoSummary: CryptoSummary,
  val reitSummary: ReitSummary,
  val materialSummary: MaterialSummary,
  val assetType: AssetType = AssetType.Stock,
  val tabs: List<AssetType> = AssetType.entries
)

class PortfolioStore(
  private val portfolioRepository: PortfolioGateway,
  initialState: PortfolioState = PortfolioState(
    stockSummary = StockSummary(items = emptyList()),
    cryptoSummary = CryptoSummary(items = emptyList()),
    reitSummary = ReitSummary(items = emptyList()),
    materialSummary = MaterialSummary(items = emptyList()),
  )
) {
  private val _state: MutableStateFlow<PortfolioState> = MutableStateFlow(initialState)

  val state: StateFlow<PortfolioState> = _state

  suspend fun load() {
    _state.value = _state.value.copy(stockSummary = _state.value.stockSummary.copy(items = portfolioRepository.stocks()))
  }

  suspend fun onSelected(assetType: Int) {
    _state.value = _state.value.copy(assetType = AssetType.entries[assetType])
    _state.value = when (_state.value.assetType) {
      AssetType.Stock -> _state.value.copy(stockSummary = _state.value.stockSummary.copy(items = portfolioRepository.stocks()))
      AssetType.Crypto -> _state.value.copy(cryptoSummary = _state.value.cryptoSummary.copy(items = portfolioRepository.cryptos()))
      AssetType.Reit -> _state.value.copy(reitSummary = _state.value.reitSummary.copy(items = portfolioRepository.reits()))
      AssetType.Material -> _state.value.copy(materialSummary = _state.value.materialSummary.copy(items = portfolioRepository.materials()))
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
    store.load()
  }

  val tabs by remember(state.tabs) {
    derivedStateOf {
      state.tabs.map {
        when (it) {
          AssetType.Stock -> R.Images.ic_stocks
          AssetType.Crypto -> R.Images.ic_crypto
          AssetType.Reit -> R.Images.ic_reit
          AssetType.Material -> R.Images.ic_materials
        }
      }
    }
  }

  Column(
    modifier = modifier
  ) {
    CustomTabs(
      selected = AssetType.entries.indexOf(state.assetType),
      onSelected = { scope.launch { store.onSelected(it) } },
      tabs = tabs
    )

    when (state.assetType) {
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
