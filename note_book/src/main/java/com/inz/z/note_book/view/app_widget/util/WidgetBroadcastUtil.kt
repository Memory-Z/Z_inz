package com.inz.z.note_book.view.app_widget.util

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.inz.z.base.util.L
import com.inz.z.note_book.R
import com.inz.z.note_book.view.app_widget.NoteInfoAppWidget
import com.inz.z.note_book.view.app_widget.bean.WidgetNoteInfo

/**
 * 小部件广播工具
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/12 09:41.
 */
object WidgetBroadcastUtil {

    private const val TAG = "WidgetBroadcastUtil"
    /**
     * 更新笔记插件
     */
    fun updateNoteWidget(context: Context?) {
        L.i(TAG, "updateNoteWidget: ---------------------------")
        if (context != null) {
            val manager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, NoteInfoAppWidget::class.java)
            val appIds = manager.getAppWidgetIds(componentName)
            manager.notifyAppWidgetViewDataChanged(appIds, R.id.app_widget_content_lv)
        }
    }


}