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

class NewsRepositoryImpl(
  private val userService: UserService
) : NewsRepository {

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
          BasicAuthCredentials(username = "1", password = "1")
        }
        realm = "Access to the '/' path"
      }
    }
    defaultRequest {
      url(localHost())
    }
  }

  override suspend fun todayNews(): List<News> =
    client.get("news").body()

}


