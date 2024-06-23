import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()
actual fun localHost(): String = "http://127.0.0.1:8080"

actual fun getIcons(): Icons {
  TODO("Not yet implemented")
}