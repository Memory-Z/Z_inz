package com.inz.z.note_book.service

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ContentResolver
import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.IBinder
import android.os.Message

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/08 14:34.
 */
class NoteInfoAppWidgetService : Service() {

    companion object {
        const val TAG = "NoteInfoAppWidgetService"
    }

    private var noteGroupId = ""

    private var noteInfoAppWidgetServiceHandler = Handler(NoteInoObserverHandlerCallback())

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        getData(intent)
        updateRemoteView()
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * 获取传递数据
     */
    private fun getData(intent: Intent?) {
        val bundle = intent?.extras
        if (bundle != null) {
            noteGroupId = bundle.getString("noteGroupId", "")
        }
    }

    /**
     * 更新 RemoteView
     */
    private fun updateRemoteView() {
        // 发送更新广播
        val broadcast = Intent()
            .apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            }
        sendBroadcast(broadcast)

    }

    /**
     * 笔记信息数据库监听
     */
    private inner class NoteInfoDBContentObserver(handler: Handler?) : ContentObserver(handler) {

        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
        }

        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)
        }

        override fun deliverSelfNotifications(): Boolean {
            return super.deliverSelfNotifications()
        }
    }

    /**
     * 笔记信息数库 Handler 回调
     */
    private inner class NoteInoObserverHandlerCallback : Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            return true
        }
    }
}