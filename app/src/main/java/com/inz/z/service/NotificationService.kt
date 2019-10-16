package com.inz.z.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.inz.z.R
import com.inz.z.view.activity.MainActivity

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/12 13:27.
 */
class NotificationService : Service() {

    companion object {
        private const val TAG = "NotificationService"
        private lateinit var notificationManager: NotificationManager
        private val notificationId = 900

    }


    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onBind(intent: Intent?): IBinder? {

        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotification("文字")
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelNotification()
    }

    private fun createNotification(channelId: String) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
        notificationBuilder.apply {
            setContentTitle("------> 标题")
            setContentInfo("内容 <--------> 特长特长特长特长特长特长特长特长特长特长特长特长特长特长特长特长特长特长特长特长特长特长特长特长特长特长特长特长特长特长特长特长特长")
            setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            setChannelId(channelId)
            setSmallIcon(R.mipmap.ic_launcher)

        }
        val notification = notificationBuilder.build().apply {
            contentIntent = pendingIntent
            flags = Notification.FLAG_NO_CLEAR
        }
        notificationManager.notify(notificationId, notification)

    }

    private fun cancelNotification() {
        notificationManager.cancel(notificationId)
    }

}