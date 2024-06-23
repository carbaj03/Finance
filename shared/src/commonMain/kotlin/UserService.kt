import kotlinx.coroutines.flow.StateFlow

interface UserService {
  val user : StateFlow<User>
  suspend fun login(username: String, password: String): Logged
  suspend fun logout() : NotLogged
}

interface ThemeService {
  val mode: StateFlow<Mode>
  fun toggle()
}

enum class Mode {
  Light,
  Dark,
}

