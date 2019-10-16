package com.inz.z.cim

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import com.farsunset.cim.sdk.android.CIMEventBroadcastReceiver
import com.farsunset.cim.sdk.android.model.Message
import com.farsunset.cim.sdk.android.CIMListenerManager
import com.farsunset.cim.sdk.android.model.ReplyBody
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import androidx.core.app.NotificationCompat


/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/09/30 09:41.
 */
class CIMPushBroadcastReceiver : CIMEventBroadcastReceiver() {

//    override fun onReceive(context: Context?, intent: Intent?) {
//        super.onReceive(context, intent)
//    }
//
//    override fun onNetworkChanged() {
//        super.onNetworkChanged()
//    }

    override fun onMessageReceived(p0: Message?, p1: Intent?) {
        //调用分发消息监听
        CIMListenerManager.notifyOnMessageReceived(p0);

        if (p0 != null) {
            //以开头的为动作消息，无须显示,如被强行下线消息Constant.ACTION_999
            if (p0.getAction().startsWith("9")) {
                return;
            }

//            showNotify(context, p0);
        }
    }

    fun onNetworkChanged(info: NetworkInfo) {
        CIMListenerManager.notifyOnNetworkChanged(info)
    }

//
//    override fun onConnectionSuccessed(hasAutoBind: Boolean) {
//        CIMListenerManager.notifyOnConnectionSuccessed(hasAutoBind)
//    }
//
//    override fun onConnectionClosed() {
//        CIMListenerManager.notifyOnConnectionClosed()
//    }
//
//
//    override fun onReplyReceived(body: ReplyBody) {
//        CIMListenerManager.notifyOnReplyReceived(body)
//    }
//
//
//    override fun onConnectionFailed() {
//        // TODO Auto-generated method stub
//        CIMListenerManager.notifyOnConnectionFailed()
//    }

    private fun showNotify(context: Context, msg: Message) {

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var channelId: String? = null
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channelId = "system"
            val channel =
                NotificationChannel(channelId, "message", NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableLights(true) //是否在桌面icon右上角展示小红点   
            notificationManager.createNotificationChannel(channel)
        }

        val title = "系统消息"
        val contentIntent = PendingIntent.getActivity(
            context,
            1,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(context, channelId!!)
        builder.setAutoCancel(true)
        builder.setDefaults(Notification.DEFAULT_ALL)
        builder.setWhen(msg.timestamp)
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setTicker(title)
        builder.setContentTitle(title)
        builder.setContentText(msg.content)
        builder.setDefaults(Notification.DEFAULT_LIGHTS)
        builder.setContentIntent(contentIntent)
        val notification = builder.build()


        notificationManager.notify(R.mipmap.ic_launcher, notification)

    }
}