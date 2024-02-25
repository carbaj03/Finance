import kotlinx.serialization.Serializable

@Serializable
sealed interface Asset {
  val ticker: String
  val added: String
  val entry: Float
  val stop: Float

  @Serializable
  data class Stock(
    override val ticker: String,
    override val added: String,
    override val entry: Float,
    override val stop: Float,
  ) : Asset

  @Serializable
  data class Crypto(
    override val ticker: String,
    override val added: String,
    override val entry: Float,
    override val stop: Float,
  ) : Asset

  @Serializable
  data class Reit(
    override val ticker: String,
    override val added: String,
    override val entry: Float,
    override val stop: Float,
  ) : Asset

  @Serializable
  data class Material(
    override val ticker: String,
    override val added: String,
    override val entry: Float,
    override val stop: Float,
  ) : Asset
}
