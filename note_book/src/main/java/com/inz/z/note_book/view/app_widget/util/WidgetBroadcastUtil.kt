package com.inz.z.note_book.view.app_widget.util

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import com.inz.z.base.util.L

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
        val intent = Intent().apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        }
        context?.sendBroadcast(intent) ?: L.w(TAG, "updateNoteWidget: context is null. ")
    }


}