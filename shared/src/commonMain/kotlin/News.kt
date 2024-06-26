import kotlinx.serialization.Serializable

@Serializable
data class News(
  val title: String,
  val label: String,
  val description: String,
  val image: String,
)