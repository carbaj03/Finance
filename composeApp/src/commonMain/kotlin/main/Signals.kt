package main

import CustomTabs
import News
import NewsRepository
import R
import Signal
import SignalRepository
import Signals
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import arrow.core.raise.either
import displayName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import painterResource

enum class SignalType {
  Registry, Notifications
}

enum class SignalFilter {
  All, Buy, Sell, Radar
}

data class SignalState(
  val signal: SignalType = SignalType.Registry,
  val filter: SignalFilter = SignalFilter.All,
  val signals: Signals = Signals(),
  val news: List<News> = emptyList()
)

class SignalStore(
  val signalRepository: SignalRepository,
  val newsRepository: NewsRepository,
  initialState: SignalState = SignalState()
) {
  private val _state = MutableStateFlow(initialState)
  val state: StateFlow<SignalState> = _state

  suspend fun load() {
    _state.value = _state.value.copy(signals = signalRepository.getSignals())
    _state.value = _state.value.copy(news = newsRepository.todayNews())
  }

  suspend fun onSignal(signal: SignalType) {
    _state.value = _state.value.copy(signal = signal)
    _state.value = when (signal) {
      SignalType.Registry -> _state.value.copy(signals = signalRepository.getSignals())
      SignalType.Notifications -> _state.value
    }
  }

  fun onFilter(filter: SignalFilter) {
    _state.value = _state.value.copy(filter = filter)
  }
}

@Composable
fun Signals(
  signalStore: SignalStore,
  modifier: Modifier = Modifier,
  allState: LazyListState,
  buyState: LazyListState,
  sellState: LazyListState,
  radarState: LazyListState
) {
  val tabs by remember { mutableStateOf(listOf(R.Images.ic_registry, R.Images.ic_notifications)) }
  val scope = rememberCoroutineScope()

  val state by signalStore.state.collectAsState()

  LaunchedEffect(Unit) {
    signalStore.load()
  }

  LazyColumn(
    modifier = modifier
  ) {
    item {
      CustomTabs(
        modifier = Modifier.fillMaxWidth(),
        selected = SignalType.entries.indexOf(state.signal),
        onSelected = { scope.launch { signalStore.onSignal(SignalType.entries[it]) } },
        tabs = tabs
      )
      Spacer(modifier = Modifier.height(16.dp))
    }

    when (state.signal) {
      SignalType.Registry -> {
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Spacer(modifier = Modifier.width(16.dp))
            FilterChip(
              label = { Text(R.Strings.all) },
              selected = state.filter == SignalFilter.All,
              onClick = { signalStore.onFilter(SignalFilter.All) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer,
              ),
            )
            FilterChip(
              label = { Text(R.Strings.buy) },
              selected = state.filter == SignalFilter.Buy,
              onClick = { signalStore.onFilter(SignalFilter.Buy) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer,
              ),
            )
            FilterChip(
              label = { Text(R.Strings.sell) },
              selected = state.filter == SignalFilter.Sell,
              onClick = { signalStore.onFilter(SignalFilter.Sell) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer,
              ),
            )
            FilterChip(
              label = { Text(R.Strings.radar) },
              selected = state.filter == SignalFilter.Radar,
              onClick = { signalStore.onFilter(SignalFilter.Radar) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer,
              ),
            )
          }

        }

        when (state.filter) {
          SignalFilter.All -> {
//            LazyColumn(
//              state = allState
//            ) {
            items(state.signals.all) { signal ->
              SignalItem(signal)
              HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.surfaceVariant)
            }
          }
//          }
          SignalFilter.Buy -> {
//            LazyColumn(
//              state = buyState
//            ) {
            items(state.signals.buy) { signal ->
              SignalItem(signal)
              HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.surfaceVariant)
            }
//            }
          }
          SignalFilter.Sell -> {
//            LazyColumn(
//              state = sellState
//            ) {
            items(state.signals.sell) { signal ->
              SignalItem(signal)
              HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.surfaceVariant)
            }
//            }
          }
          SignalFilter.Radar -> {
//            LazyColumn(
//              state = radarState
//            ) {
            items(state.signals.radar) { signal ->
              SignalItem(signal)
              HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.surfaceVariant)
            }
//            }
          }
        }
      }
      SignalType.Notifications -> {
        item {
          Text(
            text = R.Strings.news,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
          )
        }
        items(state.news) { news ->
          NewsItem(news)
          HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.surfaceVariant)
        }
      }
    }
  }
}

@Composable
fun NewsItem(
  news: News,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Column(
      modifier = Modifier.weight(1f),
      horizontalAlignment = Alignment.Start
    ) {
      Text(
        text = news.label,
        style = MaterialTheme.typography.labelMedium,
      )
      Text(
        text = news.title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
      )
      Text(
        text = news.description,
        style = MaterialTheme.typography.bodyMedium,
      )
    }
    Spacer(modifier = Modifier.width(8.dp))
    Icon(
      painter = painterResource(R.Images.ic_logo),
      contentDescription = null,
      tint = Color.White,
      modifier = Modifier.size(50.dp).background(Color.Black, RoundedCornerShape(8.dp))
    )
  }
}

@Composable
fun SignalItem(
  signal: Signal,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
//      .heightIn(min = 120.dp)
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 16.dp),
    horizontalAlignment = Alignment.Start
  ) {
    Row(
      modifier = Modifier.padding(4.dp)
    ) {
      Text(
        text = signal.assetType.displayName,
        style = MaterialTheme.typography.labelSmall,
      )
      Spacer(modifier = Modifier.weight(1f))
      Text(
        text = signal.date,
        style = MaterialTheme.typography.labelSmall,
      )
    }
    Spacer(modifier = Modifier.height(8.dp))
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
        signal.description?.let {
          Text(
            text = it,
            style = MaterialTheme.typography.bodySmall,
          )
        }
      }
    }
  }
}
