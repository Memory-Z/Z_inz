package com.inz.z.note_book.service

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.IBinder

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
//        val appWidgetManager = AppWidgetManager.getInstance(this)

    }
}