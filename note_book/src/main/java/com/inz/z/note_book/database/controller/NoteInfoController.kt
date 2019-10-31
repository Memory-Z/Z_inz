package com.inz.z.note_book.database.controller

import android.app.Activity
import com.inz.z.note_book.NoteBookApplication
import com.inz.z.note_book.bean.NoteInfo
import com.inz.z.note_book.database.NoteInfoDao

/**
 * NoteInfo 数据库控制
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/31 16:53.
 */
object NoteInfoController {
    /**
     * 获取 NoteInfoDao
     */
    private fun getNoteInfoDao(activity: Activity): NoteInfoDao? {
        val application = activity.application as NoteBookApplication
        return application.getDaoSession()?.noteInfoDao
    }

    /**
     * 查询 groupId 对应的全部 列表信息
     * @param groupId 组ID
     */
    fun findAllNoteInfoListByGroupId(activity: Activity, groupId: String): List<NoteInfo> {
        val noteInfoDao = getNoteInfoDao(activity)
        if (noteInfoDao != null) {
            return noteInfoDao.loadAll()
        }
        return emptyList()
    }

    /**
     * 通过 主键查询
     * @param noteInfoId 主键
     */
    fun findNoteInfoById(activity: Activity, noteInfoId: String): NoteInfo? {
        val noteInfoDao = getNoteInfoDao(activity)
        if (noteInfoDao != null) {
            return noteInfoDao.load(noteInfoId)
        }
        return null
    }

    /**
     * 更新 笔记
     * @param noteInfo 笔记信息
     */
    fun updateNoteInfo(activity: Activity, noteInfo: NoteInfo) {
        getNoteInfoDao(activity)?.update(noteInfo)
    }

    /**
     * 删除笔记
     * @param noteInfoId 笔记ID
     */
    fun delNoteInfoById(activity: Activity, noteInfoId: String) {
        getNoteInfoDao(activity)?.deleteByKey(noteInfoId)
    }

}