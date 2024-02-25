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