package com.inz.z.note_book.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.inz.z.base.util.L
import com.inz.z.note_book.R

/**
 * 通知 Foreground Service
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/13 16:40.
 */
class NotificationForegroundService : Service() {

    companion object {
        private const val TAG = "NotificationForegroundService"
        private const val NOTIFICATION_CODE = 0x0009
        private const val NOTIFICATION_CHANNEL_ID = "NoteBook"
    }

    private var notification: Notification? = null


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        L.i(TAG, "onCreate: --------------- ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initNotification()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }

    /**
     * 初始化通知栏
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initNotification() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            getString(R.string.app_name),
            NotificationManager.IMPORTANCE_LOW
        )
        val manager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
        notification = NotificationCompat
            .Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(applicationContext.getString(R.string.app_name))
            .setContentText(applicationContext.getString(R.string.base_content))
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setSmallIcon(R.drawable.ic_alarm_black_24dp)
            .setOngoing(true)
            .build()
        startForeground(NOTIFICATION_CODE, notification)
    }
}