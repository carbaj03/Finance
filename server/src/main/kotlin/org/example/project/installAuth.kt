package org.example.project

import GOOGLE_CLIENT_ID
import GOOGLE_CLIENT_SECRET
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.OAuthServerSettings
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.oauth
import io.ktor.server.auth.principal
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun Application.installAuth() {
  val httpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
      json()
    }
  }

  install(Sessions) {
    cookie<UserSession>("user_session")
  }

  install(Authentication) {
    oauth("auth-oauth-google") {
      urlProvider = { "http://localhost:8080/callback" }
      providerLookup = {
        OAuthServerSettings.OAuth2ServerSettings(
          name = "google",
          authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
          accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
          requestMethod = HttpMethod.Post,
          clientId = GOOGLE_CLIENT_ID,
          clientSecret = GOOGLE_CLIENT_SECRET,
          defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile")
        )
      }
      // Closeable
      client = httpClient
    }
  }

  routing {
    authenticate("auth-oauth-google") {
      get("/login") {
        // Redirects to 'authorizeUrl' automatically
      }

      get("/callback") {
        val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
        call.sessions.set(UserSession(principal?.accessToken.toString()))
        call.respondRedirect("/hello")
      }

      get("/") {
//      call.respondHtml(HttpStatusCode.OK) {
//        body {
//          p {
//            a("/login") { +"Login with Google" }
//          }
//        }
//      }
      }

//      routs()
    }
  }
//    get("/hello") {
//      val userSession: UserSession? = call.sessions.get()
//      if (userSession != null) {
//        val userInfo: UserInfo = httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
//          headers {
//            append(HttpHeaders.Authorization, "Bearer ${userSession.token}")
//          }
//        }.body()
//        call.respondText("Hello, ${userInfo.name}!")
//      } else {
//        call.respondRedirect("/")
//      }
//    }
//  }
}

data class UserSession(val token: String)

@Serializable
data class UserInfo(
  val id: String,
  val name: String,
  @SerialName("given_name") val givenName: String,
  @SerialName("family_name") val familyName: String,
  val picture: String,
  val locale: String
)