package com.example.ppt_kotders

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.ppt_kotders.controllers.NotiVictoria


class Notificacion (context: Context) {
    private var app = context
    companion object{
        const val VICTORY_CHANNEL_ID = "victoryChannel"
    }

    fun createChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                VICTORY_CHANNEL_ID,
                "MySuperChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply{
                description = "DESCRIPCION DEL CANAL"
            }

            val notificationManager: NotificationManager = app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    fun createSimpleNotification(tiempoResultado : Int){

        val intent = Intent(app, NotiVictoria::class.java).apply{
            var flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        intent.putExtra("tiempoResultado", tiempoResultado)

        val flag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent: PendingIntent = PendingIntent.getActivity(app, 0, intent, flag)

        var builder = NotificationCompat.Builder(app, VICTORY_CHANNEL_ID)
            .setSmallIcon(R.drawable.ppt)
            .setContentTitle("Victoria Piedra, Papel, Tijera")
            .setContentText("¡ VITORIA !")
            .setStyle(
                NotificationCompat.BigTextStyle()
                .bigText("Mira los datos de la partida")
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(app)) { notify(1, builder.build()) }
    }
}