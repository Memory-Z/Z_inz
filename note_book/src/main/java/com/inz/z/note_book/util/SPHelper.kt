package com.inz.z.note_book.util

import android.content.Context
import com.inz.z.base.util.SPHelper

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/08 15:03.
 */
object SPHelper {
    private var instance: SPHelper? = null
    /**
     * 初始化
     */
    fun init(context: Context) {
        synchronized(SPHelper::class.java) {
            if (instance == null) {
                synchronized(this) {
                    instance = SPHelper.getInstance()
                }
            }
        }
        instance?.initSharedPreferences(context, "NoteBook")
    }

    /**
     * 保存最后一次开启时间
     */
    fun saveLastOpenTime(lastTime: Long) {
        instance?.setSharedLong("noteLastOpenTime", lastTime)
    }

    /**
     * 获取最后一次开启时间
     */
    fun getLastOpenTime(): Long {
        return instance?.getSharedLong("noteLastOpenTime") ?: 0
    }

    /**
     * 保存对应的笔记分组ID 【桌面插件】
     */
    fun saveAppWidgetNoteGroupId(noteGroupId: String) {
        instance?.setSharedString("appWidgetNoteGroupId", noteGroupId)
    }

    /**
     * 获取笔记分组对应的ID 【桌面插件】
     */
    fun getAppWidgetNoteGroupId(): String {
        return instance?.getSharedString("appWidgetNoteGroupId") ?: ""
    }

}