package com.inz.z.note_book.view.app_widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.inz.z.base.util.L
import com.inz.z.note_book.R
import com.inz.z.note_book.bean.NoteInfo
import com.inz.z.note_book.database.controller.NoteController
import com.inz.z.note_book.database.controller.NoteGroupService
import com.inz.z.note_book.util.SPHelper
import com.inz.z.note_book.view.activity.NewNoteInfoSampleActivity
import com.inz.z.note_book.view.app_widget.bean.WidgetNoteInfo.NOTE_INFO_APP_WIDGET_CHANGE_NOTE_GROUP_ACTION
import com.inz.z.note_book.view.app_widget.bean.WidgetNoteInfo.NOTE_INFO_APP_WIDGET_ITEM_CLICK_ACTION
import com.inz.z.note_book.view.app_widget.bean.WidgetNoteInfo.NOTE_INFO_APP_WIDGET_ITEM_CLICK_NOTE_INFO_ID
import com.inz.z.note_book.view.app_widget.bean.WidgetNoteInfo.NOTE_INFO_APP_WIDGET_NOTE_GROUP_ID
import com.inz.z.note_book.view.app_widget.service.WidgetNoteInfoListRemoteViewsService


/**
 * Implementation of App Widget functionality.
 */
class NoteInfoAppWidget : AppWidgetProvider() {
    companion object {
        const val TAG = "NoteInfoAppWidget"
    }

    /**
     * 显示 笔记组ID
     */
    private var noteGroupId = ""
    private var noteInfoList: List<NoteInfo> = mutableListOf()
    private var saveAppWidgetIds: IntArray = intArrayOf()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        L.i(TAG, "onUpdate. ")
        // 获取数据信息
        initData()
        // 获取相应笔记列表
        getNoteGroup()
        saveAppWidgetIds = appWidgetIds
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        L.i(TAG, "onEnabled . ")
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        L.i(TAG, "onDisabled . ")
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        L.i(TAG, "onReceive. ---> ${intent?.action}")
        super.onReceive(context, intent)
        when (intent?.action) {
            NOTE_INFO_APP_WIDGET_ITEM_CLICK_ACTION -> {
                L.i(TAG, "-item on click .ing .")
                val bundle = intent.extras
                if (bundle != null) {
                    val noteInfoId =
                        bundle.getString(NOTE_INFO_APP_WIDGET_ITEM_CLICK_NOTE_INFO_ID, "")
                    val noteGroupId = bundle.getString(NOTE_INFO_APP_WIDGET_NOTE_GROUP_ID, "")
                    L.i(
                        TAG,
                        "onReceive: on item click noteInfoId = $noteInfoId ; noteGroupId = $noteGroupId . "
                    )
                }
            }
            NOTE_INFO_APP_WIDGET_CHANGE_NOTE_GROUP_ACTION -> {
                L.i(TAG, " change note group is click !! ")
                val manager = AppWidgetManager.getInstance(context)
                val views = RemoteViews(context?.packageName, R.layout.note_info_app_widget)
                manager.updateAppWidget(saveAppWidgetIds, views)
            }
            else -> {
                L.i(TAG, " other .do ")
            }
        }
    }

    /**
     * 初始化数据
     */
    private fun initData() {
        noteGroupId = SPHelper.getAppWidgetNoteGroupId()
    }

    /**
     * 获取笔记组
     */
    private fun getNoteGroup() {
        if (noteGroupId.isEmpty()) {
            noteGroupId = NoteGroupService.getNearUpdateNoteGroup()?.noteGroupId ?: ""
        }
        if (noteGroupId.isNotEmpty()) {
            noteInfoList = NoteController.findAllNoteInfoByGroupId(noteGroupId)
        } else {
            L.w(TAG, "_getNoteGrop: noteGroupId is empty ! can check this --> ${this}")
        }


    }

    /**
     * 更新 AppWidget 内容
     */
    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        L.i(TAG, "updateAppWidget: $appWidgetId . ")

        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.note_info_app_widget)


        // 切换分组
        val changeGroupIntent = Intent(context, NewNoteInfoSampleActivity::class.java)
            .apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    .or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                val bundle = Bundle()
                bundle.putInt("launchType", 2)
                putExtras(bundle)
            }
        val changePendingIntent = PendingIntent.getActivity(
            context,
            9,
            changeGroupIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        views.setOnClickPendingIntent(R.id.app_widget_top_title_tv, changePendingIntent)

        // 添加笔记
        val addNoteIntent = Intent(context, NewNoteInfoSampleActivity::class.java)
            .apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    .or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                val bundle = Bundle()
                bundle.putInt("launchType", 1)
                bundle.putString("groupId", noteGroupId)
                putExtras(bundle)
            }
        val addNotePendingIntent =
            PendingIntent.getActivity(context, 8, addNoteIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setOnClickPendingIntent(R.id.app_widget_top_add_iv, addNotePendingIntent)

        // 设置 ListView  Adapter
        val noteIntentService = Intent(context, WidgetNoteInfoListRemoteViewsService::class.java)
        views.setRemoteAdapter(R.id.app_widget_content_lv, noteIntentService)

        // 设置 intent 模板
        // <GrigView/ListView/StackView> 存在很多子元素，不能通过 setOnClickPendingIntent 设置点击事件
        // 1. 通过 setPendingIntentTemplate 设置 Intent 模板
        // 2. 在 RemoteViewsFactory 的 getViewAt 中，通过 setOnClickFillInIntent 设置 item 点击事件
        val listIntent = Intent()
            .apply {
                action = NOTE_INFO_APP_WIDGET_ITEM_CLICK_ACTION
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
        val pendingIntent =
            PendingIntent.getBroadcast(context, 7, listIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setPendingIntentTemplate(R.id.app_widget_content_lv, pendingIntent)


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

}


