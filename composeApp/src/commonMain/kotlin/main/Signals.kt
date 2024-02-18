package main

import CustomTabs
import R
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import domain.Signal
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

data class Signals(
  val all: List<Signal> = emptyList(),
  val buy: List<Signal> = emptyList(),
  val sell: List<Signal> = emptyList(),
  val radar: List<Signal> = emptyList(),
)

interface SignalRepository {
  suspend fun getSignals(): Signals
}

enum class AssetType {
  Stock, Crypto, Reit, Material
}

class SignalRepositoryImpl : SignalRepository {
  private val signals: List<Signal> =
    listOf(
      Signal.Buy(
        assetType = AssetType.Stock,
        date = "2021-10-10",
        title = "Buy",
        description = "Buy description"
      ),
      Signal.Sell(
        assetType = AssetType.Stock,
        date = "2021-10-10",
        title = "Sell",
        description = "Sell description"
      ),
      Signal.Radar(
        assetType = AssetType.Stock,
        date = "2021-10-10",
        title = "Radar",
        description = "Radar description"
      ),
      Signal.Buy(
        assetType = AssetType.Stock,
        date = "2021-10-10",
        title = "Buy",
        description = "Buy description"
      ),
      Signal.Sell(
        assetType = AssetType.Stock,
        date = "2021-10-10",
        title = "Sell",
        description = "Sell description"
      ),
      Signal.Radar(
        assetType = AssetType.Stock,
        date = "2021-10-10",
        title = "Radar",
        description = "Radar description"
      ),
      Signal.Buy(
        assetType = AssetType.Stock,
        date = "2021-10-10",
        title = "Buy",
        description = "Buy description"
      ),
      Signal.Sell(
        assetType = AssetType.Crypto,
        date = "2021-10-10",
        title = "Sell",
        description = "Sell description"
      ),
      Signal.Radar(
        assetType = AssetType.Crypto,
        date = "2021-10-10",
        title = "Radar",
        description = "Radar description"
      ),
      Signal.Buy(
        assetType = AssetType.Crypto,
        date = "2021-10-10",
        title = "Buy",
        description = "Buy description"
      ),
    )
  private val buy: List<Signal> = signals.filterIsInstance<Signal.Buy>()
  private val sell: List<Signal> = signals.filterIsInstance<Signal.Sell>()
  private val radar: List<Signal> = signals.filterIsInstance<Signal.Radar>()

  override suspend fun getSignals(): Signals =
    Signals(
      all = signals,
      buy = buy,
      sell = sell,
      radar = radar
    )
}

enum class SignalType {
  Registry, Notifications
}

enum class SignalFilter {
  All, Buy, Sell, Radar
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Signals(
  signalRepository: SignalRepository,
  signal: SignalType,
  onSignal: (SignalType) -> Unit,
  selectedFilter: SignalFilter,
  onSelectedFilter: (SignalFilter) -> Unit,
  modifier: Modifier = Modifier,
  allState: LazyListState,
  buyState: LazyListState,
  sellState: LazyListState,
  radarState: LazyListState
) {
  val tabs by remember { mutableStateOf(listOf(R.Images.ic_registry, R.Images.ic_notifications)) }
  var signals by remember { mutableStateOf(Signals()) }

  LaunchedEffect(Unit) {
    signals = signalRepository.getSignals()
  }

  Column(
    modifier = modifier
  ) {
    CustomTabs(
      selected = SignalType.entries.indexOf(signal),
      onSelected = { onSignal(SignalType.entries[it]) },
      tabs = tabs
    )

    Spacer(modifier = Modifier.height(16.dp))

    when (signal) {
      SignalType.Registry -> {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Spacer(modifier = Modifier.width(16.dp))
          FilterChip(
            label = { Text(R.Strings.all) },
            selected = selectedFilter == SignalFilter.All,
            onClick = { onSelectedFilter(SignalFilter.All) }
          )
          FilterChip(
            label = { Text(R.Strings.buy) },
            selected = selectedFilter == SignalFilter.Buy,
            onClick = { onSelectedFilter(SignalFilter.Buy) }
          )
          FilterChip(
            label = { Text(R.Strings.sell) },
            selected = selectedFilter == SignalFilter.Sell,
            onClick = { onSelectedFilter(SignalFilter.Sell) }
          )
          FilterChip(
            label = { Text(R.Strings.radar) },
            selected = selectedFilter == SignalFilter.Radar,
            onClick = { onSelectedFilter(SignalFilter.Radar) }
          )
        }

        when (selectedFilter) {
          SignalFilter.All -> {
            LazyColumn(
              state = allState
            ) {
              items(signals.all) { signal ->
                SignalItem(signal)
                Divider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.surfaceVariant)
              }
            }
          }
          SignalFilter.Buy -> {
            LazyColumn(
              state = buyState
            ) {
              items(signals.buy) { signal ->
                SignalItem(signal)
                Divider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.surfaceVariant)
              }
            }
          }
          SignalFilter.Sell -> {
            LazyColumn(
              state = sellState
            ) {
              items(signals.sell) { signal ->
                SignalItem(signal)
                Divider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.surfaceVariant)
              }
            }
          }
          SignalFilter.Radar -> {
            LazyColumn(
              state = radarState
            ) {
              items(signals.radar) { signal ->
                SignalItem(signal)
                Divider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.surfaceVariant)
              }
            }
          }
        }
      }

      SignalType.Notifications -> {
        Text(text = "Notifications")
      }
    }
  }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SignalItem(
  signal: Signal,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
    horizontalAlignment = Alignment.Start
  ) {
    Row(
      modifier = Modifier.padding(4.dp)
    ) {
      Text(
        text = signal.assetType.toString(),
        style = MaterialTheme.typography.labelSmall,
      )
      Spacer(modifier = Modifier.weight(1f))
      Text(
        text = signal.date,
        style = MaterialTheme.typography.labelSmall,
      )
    }
    Row {
      Icon(
        modifier = Modifier
          .background(
            when (signal) {
              is Signal.Buy -> R.Color.VintageGreen
              is Signal.Sell -> R.Color.VintageRed
              is Signal.Radar -> R.Color.VintageYellow
            }, shape = CircleShape
          )
          .padding(6.dp),
        painter = painterResource(
          when (signal) {
            is Signal.Buy -> R.Images.ic_buy
            is Signal.Sell -> R.Images.ic_sell
            is Signal.Radar -> R.Images.ic_radar
          }
        ),
        contentDescription = null,
        tint = Color.White
      )
      Column(
        modifier = Modifier.weight(1f).padding(start = 8.dp)
      ) {
        Text(
          text = signal.title,
          style = MaterialTheme.typography.bodyLarge,
        )
        Text(
          text = signal.description,
          style = MaterialTheme.typography.bodySmall,
        )
      }
    }
  }
}
