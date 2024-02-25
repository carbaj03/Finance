interface Dependencies {
  val googleTap: GoogleTap
  val userService: UserService
}

class MockDependencies : Dependencies {
  override val googleTap: GoogleTap
    get() = TODO("Not yet implemented")
  override val userService: UserService
    get() = TODO("Not yet implemented")
}