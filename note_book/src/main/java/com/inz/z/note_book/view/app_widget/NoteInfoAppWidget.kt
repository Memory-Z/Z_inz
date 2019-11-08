package com.inz.z.note_book.view.app_widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.inz.z.note_book.R
import com.inz.z.note_book.bean.NoteInfo
import com.inz.z.note_book.database.controller.NoteController
import com.inz.z.note_book.util.SPHelper

/**
 * Implementation of App Widget functionality.
 */
class NoteInfoAppWidget : AppWidgetProvider() {
    companion object {
        const val TAG = "NoteInfoAppWidget"
    }

    private var noteGroupId = ""
    private var noteInfoList: List<NoteInfo> = mutableListOf()


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // 获取数据信息
        initData()
        // 获取相应笔记列表
        _getNoteGroup()
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }

    private fun initData() {
        noteGroupId = SPHelper.getAppWidgetNoteGroupId()
    }

    private fun _getNoteGroup() {
        noteInfoList = NoteController.findAllNoteInfoByGroupId(noteGroupId)

    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.note_info_app_widget)
//    views.setTextViewText(R.id.appwidget_text, widgetText)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
