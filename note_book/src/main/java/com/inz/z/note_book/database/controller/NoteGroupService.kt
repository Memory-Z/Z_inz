package com.inz.z.note_book.database.controller

import com.inz.z.note_book.bean.NoteGroup
import com.inz.z.note_book.database.NoteGroupDao
import com.inz.z.note_book.database.util.GreenDaoHelper

/**
 * 笔记组控制
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/29 13:41.
 */
object NoteGroupService {

    /**
     * 获取 NoteGroupDao
     */
    private fun getNoteGroupDao(): NoteGroupDao? {
        return GreenDaoHelper.getInstance().getDaoSession()?.noteGroupDao
    }

    fun findAll(): List<NoteGroup> {
        return getNoteGroupDao()?.loadAll() ?: emptyList()
    }

    fun findById(id: String): NoteGroup? {
        return getNoteGroupDao()?.load(id)
    }

    /**
     * 添加分组
     */
    fun addNoteGroupWithGroupName(noteGroup: NoteGroup) {
        val noteGroupDao = getNoteGroupDao()
        if (noteGroupDao != null) {
            noteGroupDao.insert(noteGroup)
        }
    }

    /**
     * 通过组名查询分组
     */
    fun findNoteGroupByGroupName(groupName: String): NoteGroup? {
        val noteGroupDao = getNoteGroupDao()
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

    fun findNoteGroupById(groupId: String): NoteGroup? {
        val noteGroupDao = getNoteGroupDao()
        if (noteGroupDao != null) {
            return noteGroupDao.load(groupId)
        }
        return null
    }

    /**
     * 获取分组最后一个 排序
     */
    fun getLastNoteGroupOrder(): Int {
        val noteGroupDao = getNoteGroupDao()
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

    /**
     * 获取最新更新的笔记组
     */
    fun getNearUpdateNoteGroup(): NoteGroup? {
        val noteGroupDao = getNoteGroupDao()
        if (noteGroupDao != null) {
            val list = noteGroupDao.queryBuilder()
                .orderDesc(NoteGroupDao.Properties.UpdateDate)
                .limit(1)
                .list()
            return if (list.isEmpty()) null else list[0]
        }
        return null
    }
}