import androidx.compose.runtime.Composable

@Composable actual fun rememberGoogleTap(userService: UserService): GoogleTap =
    object : GoogleTap {
      override suspend fun login() {

      }

      override suspend fun logout() {

      }
    }