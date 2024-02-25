import kotlinx.serialization.Serializable

@Serializable
sealed interface Signal {
  val title: String
  val description: String
  val date: String
  val assetType: AssetType

  @Serializable
  data class Buy(
    override val title: String,
    override val description: String,
    override val date: String,
    override val assetType: AssetType,
  ) : Signal

  @Serializable
  data class Sell(
    override val title: String,
    override val description: String,
    override val date: String,
    override val assetType: AssetType,
  ) : Signal

  @Serializable
  data class Radar(
    override val title: String,
    override val description: String,
    override val date: String,
    override val assetType: AssetType,
  ) : Signal
}

