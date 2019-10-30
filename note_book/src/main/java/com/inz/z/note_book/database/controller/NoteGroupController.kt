package com.inz.z.note_book.database.controller

import android.app.Activity
import android.app.Application
import androidx.annotation.NonNull
import com.inz.z.base.util.BaseTools
import com.inz.z.note_book.NoteBookApplication
import com.inz.z.note_book.bean.NoteGroup
import com.inz.z.note_book.bean.NoteInfo
import com.inz.z.note_book.database.NoteGroupDao
import java.util.*

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/29 13:41.
 */
object NoteGroupController {

    /**
     * 获取 NoteGroupDao
     */
    private fun getNoteGroupDao(activity: Activity): NoteGroupDao? {
        val application = activity.application as NoteBookApplication
        return application.getDaoSession()?.noteGroupDao
    }

    /**
     * 通过组ID 查询信息列表
     * @param activity 查询所在Activity
     * @param groupId 组Id
     */
    @NonNull
    fun queryNoteInfoByGroupId(activity: Activity, groupId: String): MutableList<NoteInfo> {
        val noteGroupDao = getNoteGroupDao(activity)
        if (noteGroupDao != null) {
            val noteGroupList = noteGroupDao
                .queryBuilder()
                .where(NoteGroupDao.Properties.NoteGroupId.eq(groupId))
                .orderDesc(NoteGroupDao.Properties.UpdateDate)
                .list()
            if (noteGroupList.isNotEmpty()) {
                return noteGroupList[0].noteInfoList
            }
            return mutableListOf()
        }
        return mutableListOf()
    }

    /**
     * 添加分组
     */
    fun addNoteGroupWithGroupName(activity: Activity, noteGroup: NoteGroup) {
        val noteGroupDao = getNoteGroupDao(activity)
        if (noteGroupDao != null) {
            noteGroupDao.insert(noteGroup)
        }
    }

    /**
     * 通过组名查询分组
     */
    fun findNoteGroupByGroupName(activity: Activity, groupName: String): NoteGroup? {
        val noteGroupDao = getNoteGroupDao(activity)
        if (noteGroupDao != null) {
            val noteGroupList = noteGroupDao
                .queryBuilder()
                .where(NoteGroupDao.Properties.GroupName.eq(groupName))
                .list()
            if (noteGroupList.isNotEmpty()) {
                return noteGroupList[0]
            }
        }
        return null
    }

    fun findNoteGroupById(activity: Activity, groupId: String): NoteGroup? {
        val noteGroupDao = getNoteGroupDao(activity)
        if (noteGroupDao != null) {
            return noteGroupDao.load(groupId)
        }
        return null
    }

    /**
     * 获取分组最后一个 排序
     */
    fun getLastNoteGroupOrder(activity: Activity): Int {
        val noteGroupDao = getNoteGroupDao(activity)
        if (noteGroupDao != null) {
            val noteGroupList = noteGroupDao.queryBuilder().orderDesc(NoteGroupDao.Properties.Order)
                .limit(1)
                .list()
            if (noteGroupList.isNotEmpty()) {
                return noteGroupList[0].order
            }
        }
        return 0
    }
}