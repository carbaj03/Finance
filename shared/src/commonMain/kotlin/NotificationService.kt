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
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json

class NotificationService {
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

  suspend fun send(messageDto: SendMessageDto) = client.post("/send") {
    contentType(ContentType.Application.Json)
    setBody(messageDto)
  }.body<Unit>()

  suspend fun broadcast(messageDto: SendMessageDto) = client.post("/broadcast") {
    contentType(ContentType.Application.Json)
    setBody(messageDto)
  }.body<Unit>()

}