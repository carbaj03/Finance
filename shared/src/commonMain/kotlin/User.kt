sealed interface User
data class Logged(val username: String) : User
data object NotLogged : User
