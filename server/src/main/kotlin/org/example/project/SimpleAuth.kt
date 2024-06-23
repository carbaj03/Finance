package org.example.project

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.basic
import io.ktor.server.routing.Route
import io.ktor.server.routing.routing

private const val Name = "auth-basic"

fun Application.basic(
  routing: Route.() -> Unit
) {
  install(Authentication) {
    basic(Name) {
      realm = "Access to the '/' path"
      validate { credentials ->
        if (credentials.name == "carbaj03" && credentials.password == "password123") {
          UserIdPrincipal(credentials.name)
        } else {
          null
        }
      }
    }
  }
  routing {
    authenticate(Name) {
      routing(this@routing)
    }
  }
}