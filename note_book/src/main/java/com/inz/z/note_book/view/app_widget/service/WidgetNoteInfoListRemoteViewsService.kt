package com.inz.z.note_book.view.app_widget.service

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.inz.z.base.util.BaseTools
import com.inz.z.base.util.L
import com.inz.z.note_book.R
import com.inz.z.note_book.bean.NoteInfo
import com.inz.z.note_book.database.controller.NoteController
import com.inz.z.note_book.database.controller.NoteGroupService
import com.inz.z.note_book.util.SPHelper
import com.inz.z.note_book.view.app_widget.bean.WidgetNoteInfo.NOTE_INFO_APP_WIDGET_ITEM_CLICK_NOTE_INFO_ID
import com.inz.z.note_book.view.app_widget.bean.WidgetNoteInfo.NOTE_INFO_APP_WIDGET_NOTE_GROUP_ID

/**
 * 小部件 笔记列表更新服务
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/11 10:20.
 */
class WidgetNoteInfoListRemoteViewsService : RemoteViewsService() {
    companion object {
        const val TAG = "WidgetNoteInfoListRVS"
    }

    init {
        L.i(TAG, "init .......... ")
    }

    override fun onCreate() {
        super.onCreate()
        L.i(TAG, "onCreate..")
    }

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        L.i(TAG, "onGetViewFactory")
        val appWidgetId = intent?.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
        return WidgetNoteInfoListRemoteViewsServiceFactory(applicationContext, appWidgetId)
    }

    private inner class WidgetNoteInfoListRemoteViewsServiceFactory(
        private var mContext: Context?,
        appWidgetId: Int?
    ) : RemoteViewsFactory {

        private var noteInfoList: MutableList<NoteInfo> = mutableListOf()
        private var noteGroupId = ""

        init {
            L.i(TAG, "WidgetNoteInfoListRVSFactory init . $appWidgetId")
        }


        override fun onCreate() {
            L.i(TAG, "onCreate. ")
            noteGroupId = SPHelper.getAppWidgetNoteGroupId()
            initNoteInfoData()
        }

        override fun getLoadingView(): RemoteViews? {
            L.i(TAG, "getLoadingView. ")
            return null
        }

        override fun getItemId(position: Int): Long {
            L.i(TAG, "getItemId: $position")
            return position.toLong()
        }

        override fun onDataSetChanged() {
            L.i(TAG, "onDataSetChanged .")
            updateNoteInfoData()
        }

        override fun hasStableIds(): Boolean {
            L.i(TAG, "hasStableIds .")
            return true
        }

        override fun getViewAt(position: Int): RemoteViews {
            val noteInfo = noteInfoList[position]
            L.i(TAG, "getViewAt: $position ---- $noteInfo")
            val remoteViews =
                RemoteViews(mContext?.packageName, R.layout.widget_item_note_sample_layout)
            val updateDateStr = BaseTools.getBaseDateFormat().format(noteInfo.updateDate)
            remoteViews.setTextViewText(R.id.item_note_sample_title_tv, noteInfo.noteTitle)
            remoteViews.setTextViewText(R.id.item_note_sample_update_date_tv, updateDateStr)
            remoteViews.setViewVisibility(R.id.item_note_sample_update_date_tv, View.GONE)
            val clickIntent = Intent()
                .apply {
                    val bundle = Bundle()
                        .apply {
                            putString(
                                NOTE_INFO_APP_WIDGET_ITEM_CLICK_NOTE_INFO_ID,
                                noteInfo.noteInfoId
                            )
                            putString(NOTE_INFO_APP_WIDGET_NOTE_GROUP_ID, noteGroupId)
                        }
                    putExtras(bundle)
                }

            remoteViews.setOnClickFillInIntent(R.id.item_note_sample_ll, clickIntent)

            return remoteViews
        }

        override fun getCount(): Int {
            L.i(TAG, "getCount . ${noteInfoList.size} ")
            return noteInfoList.size
        }

        override fun getViewTypeCount(): Int {
            L.i(TAG, "getViewTypeCount, ")
            return 1
        }

        override fun onDestroy() {
            L.i(TAG, "onDestroy. ")
        }

        /**
         * 初始化 NoteInfo 数据
         */
        private fun initNoteInfoData() {
            L.i(TAG, "initNoteInfoData ----------------- ")
            if (noteGroupId.isEmpty()) {
                noteGroupId = NoteGroupService.getNearUpdateNoteGroup()?.noteGroupId ?: ""
            }
//            if (noteGroupId.isEmpty()) {
//                L.w(TAG, "initNoteInfoData: noteGroupId is null . ---> ${this}")
//                return
//            }
//            noteInfoList =
//                NoteController.findAllNoteInfoByGroupId(noteGroupId) as MutableList<NoteInfo>

        }

        /**
         * 更新
         */
        private fun updateNoteInfoData() {
            if (noteGroupId.isEmpty()) {
                L.w(TAG, "updateNoteInfoData: noteGroupId is null . ---> ${this}")
                return
            }
            noteInfoList =
                NoteController.findAllNoteInfoByGroupId(noteGroupId) as MutableList<NoteInfo>
        }
    }


}