package org.acv.hopla

import App
import Mode
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

  private val requestPermissionLauncher = registerForActivityResult(
    ActivityResultContracts.RequestPermission(),
  ) { isGranted: Boolean ->
    if (isGranted) {
      Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
    } else {
      Toast.makeText(
        this,
        "FCM can't post notifications without POST_NOTIFICATIONS permission",
        Toast.LENGTH_LONG,
      ).show()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      // Create channel to show notifications.
      val channelId = getString(R.string.default_notification_channel_id)
      val channelName = getString(R.string.default_notification_channel_name)
      val notificationManager = getSystemService(NotificationManager::class.java)
      notificationManager?.createNotificationChannel(
        NotificationChannel(
          channelId,
          channelName,
          NotificationManager.IMPORTANCE_LOW,
        ),
      )
    }

    intent?.extras?.let {
      for (key in it.keySet()) {
        val value = intent.extras?.getString(key)
        Log.d("MainActivity", "Key: $key Value: $value")
      }
    }

    askNotificationPermission()

    setContent {
      var theme: Mode by remember { mutableStateOf(Mode.Light) }

      DisposableEffect(theme) {
        enableEdgeToEdge(
          statusBarStyle = when (theme) {
            Mode.Light -> SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
            Mode.Dark -> SystemBarStyle.dark(Color.TRANSPARENT)
          },
          navigationBarStyle = when (theme) {
            Mode.Light -> SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
            Mode.Dark -> SystemBarStyle.dark(Color.TRANSPARENT)
          },
        )
        onDispose {}
      }

      App(onThemeChange = { theme = it })
    }
  }

  private fun askNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
        // FCM SDK (and your app) can post notifications.
      } else {
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
      }
    }
  }
}

@Preview
@Composable
fun AppAndroidPreview() {
  App({})
}