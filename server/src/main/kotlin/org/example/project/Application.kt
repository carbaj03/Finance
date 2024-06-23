package org.example.project

import Asset.Crypto
import Asset.Stock
import AssetType
import News
import SendMessageDto
import Signal
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun main() {
  embeddedServer(Netty, port = 8080) {
    install(ContentNegotiation) {
      json(Json {
        prettyPrint = true
        isLenient = true
      })
    }
    val serviceAccountStream = this::class.java.classLoader.getResourceAsStream("service_account_key.json")
    val options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(serviceAccountStream)).build()
    val app = FirebaseApp.initializeApp(options)
    val messaging = FirebaseMessaging.getInstance(app)

    basic(messaging)
//    installAuth()
  }.start(wait = true)
}

fun Application.basic(messaging: FirebaseMessaging) {
  install(Authentication) {
    basic("auth-basic") {
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
    authenticate("auth-basic") {
      routs(messaging)
    }
  }
}

fun Route.routs(messaging: FirebaseMessaging) {
  main()
  sendNotification(messaging)
  signals()
  stocks()
  cryptos()
  news()
}

private fun Route.news() {
  get("/news") {
    call.respond(
      listOf(
        News(
          title = "BTCUSD: el ETF de BlackRock supera los 105.000 bitcoins.",
          label = "15/02/2024 | HOPLA",
          description = "Un hito para el gigante de inversiones al alcanzar una cantidad masiva de bitcoins.",
          image = "https://example.com/image1.jpg"
        ),
        News(
          title = "Japón, Alemania y Reino Unido, en recesión. Y China con un débil consumo doméstico.",
          label = "15/02/2024 | HOPLA",
          description = "Los banqueros centrales no pueden subir los tipos en este entorno de debilidad.",
          image = "https://example.com/image2.jpg"
        ),
        News(
          title = "Los inversores están acumulando opciones call de valores individuales.",
          label = "12/02/2024 | HOPLA",
          description = "Estrategia de acumulación ante la expectativa de una subida del mercado.",
          image = "https://example.com/image3.jpg"
        ),
        News(
          title = "El muro de las opciones call ha vuelto a la zona de los 5000.",
          label = "12/02/2024 | HOPLA",
          description = "Mañana se producirá una fuerte expiración de opciones, un evento crítico para los traders.",
          image = "https://example.com/image4.jpg"
        ),
        // Continuation of fictional news items...
        News(
          title = "La inteligencia artificial revoluciona la medicina personalizada.",
          label = "10/02/2024 | HOPLA",
          description = "Avances en IA permiten tratamientos altamente personalizados para enfermedades crónicas.",
          image = "https://example.com/image5.jpg"
        ),
        News(
          title = "Nueva ola de startups de energías renovables capta la atención de inversores.",
          label = "09/02/2024 | HOPLA",
          description = "El enfoque en la sostenibilidad atrae financiamiento para innovaciones verdes.",
          image = "https://example.com/image6.jpg"
        ),
        // ... Additional fictional news items would follow
      )
    )
  }
}

private fun Route.cryptos() {
  get("/cryptos") {
    call.respond(
      listOf(
        Crypto("BTCUSD", name = "Bitcoin/Usd","05/01/2024", 60000f, 58000f),
        Crypto("ETHUSD", name = "Etherium/Usd","19/01/2024", 2000f, 1900f),
      )
    )
  }
}

private fun Route.stocks() {
  get("/stocks") {
    call.respond(
      listOf(
        Stock(ticker = "AAPL", name = "Apple", added = "05/01/2024", entry = 170f, stop = 160f),
        Stock(ticker = "GOOGL", name = "Google", added = "19/01/2024", entry = 2800f, stop = 2750f),
        Stock(ticker = "MSFT", name = "Microsoft", added = "28/01/2024", entry = 300f, stop = 290f),
        Stock(ticker = "AMZN", name = "Amazon", added = "26/01/2024", entry = 3200f, stop = 3100f),
        Stock(ticker = "FB", name = "Facebook", added = "25/01/2024", entry = 250f, stop = 240f),
        Stock(ticker = "TSLA", name = "Tesla", added = "03/01/2024", entry = 800f, stop = 780f),
        Stock(ticker = "NFLX", name = "Netflix", added = "09/01/2024", entry = 500f, stop = 485f),
        Stock(ticker = "NVDA", name = "Nvidia", added = "04/01/2024", entry = 700f, stop = 690f),
        Stock(ticker = "INTC", name = "Intel", added = "16/01/2024", entry = 50f, stop = 48f),
        Stock(ticker = "AMD", name = "AMD", added = "25/01/2024", entry = 100f, stop = 95f),
        Stock(ticker = "BABA", name = "Alibaba", added = "15/01/2024", entry = 150f, stop = 145f),
        Stock(ticker = "SONY", name = "Sony", added = "16/01/2024", entry = 80f, stop = 78f),
        Stock(ticker = "SNE", name = "Sony", added = "21/01/2024", entry = 85f, stop = 82f),
        Stock(ticker = "IBM", name = "IBM", added = "13/01/2024", entry = 130f, stop = 128f),
        Stock(ticker = "HPQ", name = "HP", added = "26/01/2024", entry = 30f, stop = 28f),
        Stock(ticker = "XOM", name = "Exxon", added = "07/01/2024", entry = 60f, stop = 58f),
        Stock(ticker = "CVX", name = "Chevron", added = "04/01/2024", entry = 120f, stop = 118f),
        Stock(ticker = "PG", name = "Procter & Gamble", added = "16/01/2024", entry = 140f, stop = 138f),
        Stock(ticker = "KO", name = "Coca-Cola", added = "01/01/2024", entry = 55f, stop = 54f),
        Stock(ticker = "PEP", name = "Pepsi", added = "29/01/2024", entry = 140f, stop = 135f),
        Stock(ticker = "MCD", name = "McDonald's", added = "27/01/2024", entry = 230f, stop = 225f),
        Stock(ticker = "WMT", name = "Walmart", added = "13/01/2024", entry = 140f, stop = 138f),
        Stock(ticker = "DIS", name = "Disney", added = "14/01/2024", entry = 100f, stop = 98f),
        Stock(ticker = "V", name = "Visa", added = "20/01/2024", entry = 210f, stop = 205f),
        Stock(ticker = "MA", name = "Mastercard", added = "25/01/2024", entry = 330f, stop = 325f)
      )
    )
  }
}

private fun Route.signals() {
  get("/signals") {
    call.respond(
      listOf(
        Signal.Buy(
          assetType = AssetType.Crypto,
          date = "2021-10-10",
          title = "BTCUSD añadido a la cartera",
          description = "Reentrada tras corrección"
        ),
        Signal.Sell(
          assetType = AssetType.Stock,
          date = "2021-10-10",
          title = "SMCI vendido",
          description = null
        ),
        Signal.Radar(
          assetType = AssetType.Stock,
          date = "2021-10-10",
          title = "PGR vigilando ...",
          description = null
        ),
        Signal.Buy(
          assetType = AssetType.Stock,
          date = "2021-10-10",
          title = "Buy",
          description = "Buy description"
        ),
        Signal.Sell(
          assetType = AssetType.Stock,
          date = "2021-10-10",
          title = "Sell",
          description = "Sell description"
        ),
        Signal.Radar(
          assetType = AssetType.Stock,
          date = "2021-10-10",
          title = "Radar",
          description = "Radar description"
        ),
        Signal.Buy(
          assetType = AssetType.Stock,
          date = "2021-10-10",
          title = "Buy",
          description = "Buy description"
        ),
        Signal.Sell(
          assetType = AssetType.Crypto,
          date = "2021-10-10",
          title = "Sell",
          description = "Sell description"
        ),
        Signal.Radar(
          assetType = AssetType.Crypto,
          date = "2021-10-10",
          title = "Radar",
          description = "Radar description"
        ),
        Signal.Buy(
          assetType = AssetType.Crypto,
          date = "2021-10-10",
          title = "Buy",
          description = "Buy description"
        ),
      )
    )
  }
}

private fun Route.main() {
  get("/") {
    call.respondText("Hello, ${call.principal<UserIdPrincipal>()?.name}!")
  }
}

fun Route.sendNotification(messaging: FirebaseMessaging) {
  post("/send") {
    val body = call.receiveNullable<SendMessageDto>() ?: kotlin.run {
      call.respond(HttpStatusCode.BadRequest)
      return@post
    }

    messaging.send(body.toMessage())

    call.respond(HttpStatusCode.OK)
  }

  post("/broadcast") {
    val body = call.receiveNullable<SendMessageDto>() ?: kotlin.run {
      call.respond(HttpStatusCode.BadRequest)
      return@post
    }

    messaging.send(body.toMessage())

    call.respond(HttpStatusCode.OK)
  }
}

fun SendMessageDto.toMessage(): Message {
  return Message.builder()
    .setNotification(
      Notification.builder()
        .setTitle(notification.title)
        .setBody(notification.body)
        .build()
    )
    .apply {
      if (to == null) {
        setTopic("chat")
      } else {
        setToken(to)
      }
    }
    .build()
}