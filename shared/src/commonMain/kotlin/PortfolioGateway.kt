import Asset.Crypto
import Asset.Material
import Asset.Reit
import Asset.Stock

interface PortfolioGateway {
  suspend fun stocks(): List<Stock>
  suspend fun cryptos(): List<Crypto>
  suspend fun reits(): List<Reit>
  suspend fun materials(): List<Material>
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
  override val expiration: String = "31días",
  override val dropdown: String = "9.5%"
) : Summary

data class CryptoSummary(
  override val title: String = "Cripto",
  override val items: List<Crypto>,
  override val performance: String = "2.5%",
  override val expiration: String = "15días",
  override val dropdown: String = "2.5%"
) : Summary

data class ReitSummary(
  override val title: String = "Reit",
  override val items: List<Reit>,
  override val performance: String = "1.5%",
  override val expiration: String = "45días",
  override val dropdown: String = "1.5%"
) : Summary

data class MaterialSummary(
  override val title: String = "Materials",
  override val items: List<Material>,
  override val performance: String = "4.5%",
  override val expiration: String = "60días",
  override val dropdown: String = "4.5%"
) : Summary