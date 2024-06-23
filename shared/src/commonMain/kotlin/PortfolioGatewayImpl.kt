import Asset.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json

class PortfolioGatewayImpl(
  private val userService : UserService
) : PortfolioGateway {

  private val client = HttpClient {
    install(ContentNegotiation) {
      json()
    }
    install(Logging) {
      logger = Logger.DEFAULT
      level = LogLevel.HEADERS
    }
    install(Auth) {
      basic {
        credentials {
          BasicAuthCredentials(username = "carbaj03", password = "password123")
        }
        realm = "Access to the '/' path"
      }
    }
    defaultRequest {
      url(localHost())
    }
  }

  override suspend fun stocks(): List<Stock> =
    client.get("stocks").body()

  override suspend fun cryptos(): List<Crypto> =
    client.get("cryptos").body()

  override suspend fun reits(): List<Reit> =
    listOf(
      Reit("O", name = "Realty Income", "2021-01-01", 100f, 90f),
      Reit("STOR", name = "STORE Capital", "2021-01-01", 900f, 890f)
    )

  override suspend fun materials(): List<Material> =
    listOf(
      Material("X", name = "United States Steel", "2021-01-01", 100f, 90f),
      Material("NUE", name = "Nucor", "2021-01-01", 900f, 890f)
    )
}


