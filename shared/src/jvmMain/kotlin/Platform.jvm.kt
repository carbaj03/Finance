class JVMPlatform : Platform {
  override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()
actual fun localHost(): String = "http://localhost:8080"

class JVMIcons : Icons {
  override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getIcons(): Icons = JVMIcons()