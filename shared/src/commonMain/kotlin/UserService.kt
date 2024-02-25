import kotlinx.coroutines.flow.StateFlow

interface UserService {
  val user : StateFlow<User>
  suspend fun login(username: String, password: String): Logged
  suspend fun logout() : NotLogged
}