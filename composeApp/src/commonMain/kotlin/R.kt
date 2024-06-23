import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

interface Strings {
  val dropdown: String
  val expiration: String
  val performance: String
  val stocks: String

  val all: String
  val buy: String
  val sell: String
  val radar: String

  val signals: String
  val portfolio: String
  val historical: String
  val system: String
}

object R {

  object Strings {
    const val news: String = "Noticias relevantes para hoy"
    const val dropdown: String = "Máxima Perdida"
    const val expiration: String = "Vencimiento Media"
    const val performance: String = "Rentavilidad Media"
    const val stocks = "Acciones"

    const val all = "Todos"
    const val buy = "Comprar"
    const val sell = "Vender"
    const val radar = "Radar"

    const val signals = "Señales"
    const val portfolio = "Cartera"
    const val historical = "Historial"
    const val system = "Sistema"

  }

  object Images {
    const val ic_user = "face.png"

    const val DarkMode: String = "ic_dark_mode.xml"
    const val LightMode: String = "ic_light_mode.xml"
    const val ArrowUp = "ic_arrow_up.xml"
    const val ArrowDown = "ic_arrow_down.xml"

    const val ic_logo = "ic_hopla.xml"

    const val ic_alerts = "ic_alerts.xml"
    const val ic_alerts_disabled = "ic_alerts_disabled.xml"
    const val ic_portfolio = "ic_portfolio.xml"
    const val ic_portfolio_disabled = "ic_portfolio_disabled.xml"
    const val ic_historical = "ic_historical.xml"
    const val ic_historical_disabled = "ic_historical_disabled.xml"
    const val ic_system = "ic_system.xml"
    const val ic_system_disabled = "ic_system_disabled.xml"

    const val ic_crypto = "ic_crypto.xml"
    const val ic_materials = "ic_materials.xml"
    const val ic_reit = "ic_reit.xml"
    const val ic_stocks = "ic_stocks.xml"
    const val ic_registry = "ic_registry.xml"
    const val ic_notifications = "ic_notifications.xml"

    const val ic_sell = "ic_sell.xml"
    const val ic_buy = "ic_buy.xml"
    const val ic_radar = "ic_radar.xml"
  }

  object Color {

    val White = Color(0xFFFFFFFF)
    val Black = Color(0xFF000000)
    val Transparent = Color(0x00000000)
    val Grey = Color(0xFF808080)

    val RedPastel = Color(0xFFD9A3A3)
    val GreenPastel = Color(0xFFA3D9A3)
    val BluePastel = Color(0xFFA3A3D9)
    val YellowPastel = Color(0xFFD9D9A3)
    val PurplePastel = Color(0xFFD9A3D9)
    val OrangePastel = Color(0xFFD9BFA3)
    val PinkPastel = Color(0xFFD9A3BF)
    val CyanPastel = Color(0xFFA3D9D9)

    val LightRed = Color(0xFFFFA3A3)
    val LightGreen = Color(0xFFA3FFA3)
    val LightBlue = Color(0xFFA3A3FF)
    val LightYellow = Color(0xFFFFFFA3)
    val LightPurple = Color(0xFFFFA3FF)
    val LightOrange = Color(0xFFFFDFA3)
    val LightPink = Color(0xFFFFA3DF)
    val LightCyan = Color(0xFFA3FFFF)
    val LightGrey = Color(0xFFD3D3D3)

    val VintageRed = Color(0xFFB7410E)
    val VintageGreen = Color(0xFF6B8E23)
    val VintageBlue = Color(0xFF6495ED)
    val VintageYellow = Color(0xFFDAA520)
    val VintagePurple = Color(0xFF800080)
    val VintageOrange = Color(0xFFFFA500)
    val VintagePink = Color(0xFFFFC0CB)
    val VintageCyan = Color(0xFF20B2AA)
  }
}