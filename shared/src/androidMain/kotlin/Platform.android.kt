import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()
//actual fun localHost(): String = "http://192.168.0.101:8080"
actual fun localHost(): String = "http://10.0.2.2:8080"

class AndroidIcons() : Icons

actual fun getIcons(): Icons = AndroidIcons()

