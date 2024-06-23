package org.acv.hopla

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

  override fun onMessageReceived(remoteMessage: RemoteMessage) {

    Log.d(TAG, "From: ${remoteMessage.from}")

    if (remoteMessage.data.isNotEmpty()) {
      Log.d(TAG, "Message data payload: ${remoteMessage.data}")

      if (isLongRunningJob()) {
        scheduleJob()
      } else {
        handleNow()
      }
    }

    remoteMessage.notification?.let {
      Log.d(TAG, "Message Notification Body: ${it.body}")
      it.body?.let { body -> sendNotification(body, it.title) }
    }

    // Also if you intend on generating your own notifications as a result of a received FCM
    // message, here is where that should be initiated. See sendNotification method below.
  }

  private fun isLongRunningJob() = true

  override fun onNewToken(token: String) {
    Log.d(TAG, "Refreshed token: $token")

    sendRegistrationToServer(token)
  }

  private fun scheduleJob() {
    val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
    WorkManager.getInstance(this).beginWith(work).enqueue()
  }

  private fun handleNow() {
    Log.d(TAG, "Short lived task is done.")
  }

  /**
   * Persist token to third-party servers.
   *
   * Modify this method to associate the user's FCM registration token with any server-side account
   * maintained by your application.
   *
   * @param token The new token.
   */
  private fun sendRegistrationToServer(token: String?) {
    Log.d(TAG, "sendRegistrationTokenToServer($token)")
  }

  /**
   * Create and show a simple notification containing the received FCM message.
   *
   * @param messageBody FCM message body received.
   */
  private fun sendNotification(messageBody: String, title : String?) {
    val requestCode = 0
    val intent = Intent(this, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    val pendingIntent = PendingIntent.getActivity(
      this,
      requestCode,
      intent,
      PendingIntent.FLAG_IMMUTABLE,
    )

    val channelId = getString(R.string.default_notification_channel_id)
    val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val notificationBuilder = NotificationCompat.Builder(this, channelId)
      .setSmallIcon(R.drawable.ic_hopla)
      .setContentTitle(title ?: getString(R.string.fcm_message))
      .setContentText(messageBody)
      .setAutoCancel(true)
      .setSound(defaultSoundUri)
      .setContentIntent(pendingIntent)

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channel = NotificationChannel(
        channelId,
        "Channel human readable title",
        NotificationManager.IMPORTANCE_DEFAULT,
      )
      notificationManager.createNotificationChannel(channel)
    }

    val notificationId = 0
    notificationManager.notify(notificationId, notificationBuilder.build())
  }

  companion object {
    private const val TAG = "MyFirebaseMsgService"
  }
}