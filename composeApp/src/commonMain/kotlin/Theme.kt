import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun Theme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = true,
  mode: Mode,
  content: @Composable () -> Unit
) {
  val colorScheme = when (mode) {
    Mode.Light -> lightColorScheme().copy(
      primary = R.Color.Black,
      onPrimary = R.Color.White,
      primaryContainer = R.Color.Black,
      onPrimaryContainer = R.Color.White,
      secondaryContainer = R.Color.LightGrey,
      onSecondaryContainer = R.Color.Black,
      tertiaryContainer = R.Color.LightGrey,
      onTertiaryContainer = R.Color.Black,
      onBackground = R.Color.Black,
    )
    Mode.Dark -> darkColorScheme().copy(
      primary = R.Color.White,
      onPrimary = R.Color.Black,
      primaryContainer = R.Color.White,
      onPrimaryContainer = R.Color.Black,
      secondaryContainer = R.Color.Grey,
      onSecondaryContainer = R.Color.White,
      tertiaryContainer = R.Color.LightGrey,
      onTertiaryContainer = R.Color.Black,
      onBackground = R.Color.White,
    )
  }

  MaterialTheme(
    colorScheme = colorScheme,
    content = content
  )
}