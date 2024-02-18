package domain

import main.AssetType

sealed interface Signal {
  val title: String
  val description: String
  val date: String
  val assetType: AssetType

  data class Buy(
    override val title: String,
    override val description: String,
    override val date: String,
    override val assetType: AssetType,
  ) : Signal

  data class Sell(
    override val title: String,
    override val description: String,
    override val date: String,
    override val assetType: AssetType,
  ) : Signal

  data class Radar(
    override val title: String,
    override val description: String,
    override val date: String,
    override val assetType: AssetType,
  ) : Signal
}

sealed interface Asset {
  val ticker: String
  val added: String
  val entry: Float
  val stop: Float

  data class Stock(
    override val ticker: String,
    override val added: String,
    override val entry: Float,
    override val stop: Float,
  ) : Asset

  data class Crypto(
    override val ticker: String,
    override val added: String,
    override val entry: Float,
    override val stop: Float,
  ) : Asset

  data class Reit(
    override val ticker: String,
    override val added: String,
    override val entry: Float,
    override val stop: Float,
  ) : Asset

  data class Material(
    override val ticker: String,
    override val added: String,
    override val entry: Float,
    override val stop: Float,
  ) : Asset
}
