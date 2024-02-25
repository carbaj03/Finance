import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.formUrlEncode
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json

val GOOGLE_CLIENT_ID = "349465254302-d8g1pkkpmck2t5ifjvje836cao1ei8nc.apps.googleusercontent.com"
val GOOGLE_CLIENT_SECRET = "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"

suspend fun client() {
  val authorizationUrlQuery = parameters {
    append("client_id", GOOGLE_CLIENT_ID)
    append("scope", "https://www.googleapis.com/auth/userinfo.profile")
    append("response_type", "code")
    append("redirect_uri", "http://0.0.0.0:8080")
    append("access_type", "offline")
  }.formUrlEncode()
  println("https://accounts.google.com/o/oauth2/auth?$authorizationUrlQuery")
  println("Open a link above, get the authorization code, insert it below, and press Enter.")
  val authorizationCode = readln()

  // Step 2: Create a storage for tokens
  val bearerTokenStorage = mutableListOf<BearerTokens>()

  // Step 3: Configure the client for receiving tokens and accessing the protected API
  val client = HttpClient {
    install(ContentNegotiation) {
      json()
    }

    install(Auth) {
      bearer {
        loadTokens {
          bearerTokenStorage.last()
        }
        refreshTokens {
          val refreshTokenInfo: TokenInfo = client.submitForm(
            url = "https://accounts.google.com/o/oauth2/token",
            formParameters = parameters {
              append("grant_type", "refresh_token")
              append("client_id", GOOGLE_CLIENT_ID)
              append("refresh_token", oldTokens?.refreshToken ?: "")
            }
          ) { markAsRefreshTokenRequest() }.body()
          bearerTokenStorage.add(BearerTokens(refreshTokenInfo.accessToken, oldTokens?.refreshToken!!))
          bearerTokenStorage.last()
        }
        sendWithoutRequest { request ->
          request.url.host == "www.googleapis.com"
        }
      }
    }
  }

  // Step 4: Exchange the authorization code for tokens and save tokens in the storage
  val tokenInfo: TokenInfo = client.submitForm(
    url = "https://accounts.google.com/o/oauth2/token",
    formParameters = parameters {
      append("grant_type", "authorization_code")
      append("code", authorizationCode)
      append("client_id", GOOGLE_CLIENT_ID)
      append("client_secret", "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")
      append("redirect_uri", "http://0.0.0.0:8080")
    }
  ).body()
  bearerTokenStorage.add(BearerTokens(tokenInfo.accessToken, tokenInfo.refreshToken!!))

  // Step 5: Make a request to the protected API
  while (true) {
    println("Make a request? Type 'yes' and press Enter to proceed.")
    when (readln()) {
      "yes" -> {
        val response: HttpResponse = client.get("https://www.googleapis.com/oauth2/v2/userinfo")
        try {
          val userInfo: UserInfo = response.body()
          println("Hello, ${userInfo.name}!")
        } catch (e: Exception) {
          val errorInfo: ErrorInfo = response.body()
          println(errorInfo.error.message)
        }
      }
      else -> return
    }
  }
}