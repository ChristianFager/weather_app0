package com.example.weather_app
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class service : Service()
{
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int
    {
        onTaskRemoved(intent)
        Toast.makeText(applicationContext, "This is a Service running in Background", Toast.LENGTH_SHORT).show()
        make_notification()
        return START_STICKY
    }


    override fun onBind(intent: Intent): IBinder?
    {
        throw UnsupportedOperationException("Error")
    }


    override fun onTaskRemoved(rootIntent: Intent)
    {
        val restartServiceIntent = Intent(applicationContext, this.javaClass)
        restartServiceIntent.setPackage(packageName)
        startService(restartServiceIntent)
        super.onTaskRemoved(rootIntent)
    }


    fun make_notification()
    {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val id: String = "my_channel_01"
        val notification_id = 101

        val weather_text: String = "Weather Updated"
        var builder = NotificationCompat.Builder(this, id)
            //.setSmallIcon(R.drawable.notification_icon)
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
            .setContentTitle("Weather App")
            .setContentText(weather_text)
            .setStyle(
                NotificationCompat.BigTextStyle()
                .bigText(weather_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(notification_id, builder.build())
        }
    }
}