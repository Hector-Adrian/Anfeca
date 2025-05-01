package com.example.anfeca.datos

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.anfeca.R


class Notificaciones(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        enviarNotificacion()
        return Result.success()
    }

    private fun enviarNotificacion() {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "daily_channel"
        val channelName = "Daily Notifications"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Recordatorio semanal")
            .setContentText("No te olvides de mí, ponte a repasar cabrón :c")
            .setSmallIcon(R.drawable.ic_notification) // Asegúrate de tener este icono
            .build()

        notificationManager.notify(1, notification)
    }
}



