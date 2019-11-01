package com.inz.z.note_book.database.controller

import com.inz.z.base.util.BaseTools
import com.inz.z.note_book.bean.NoteGroupWithInfo
import com.inz.z.note_book.bean.NoteInfo

/**
 *
 * 笔记控制管理
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/01 10:12.
 */
object NoteController {

    /**
     * 添加笔记
     */
    fun addNoteInfo(noteGroupId: String, noteInfo: NoteInfo): Boolean {
        val noteGroupInfo = NoteGroupService.findNoteGroupById(noteGroupId)
        if (noteGroupInfo != null) {
            NoteInfoController.insert(noteInfo)
            val insertNote = NoteInfoController.findById(noteInfo.noteInfoId)
            if (insertNote != null) {
                val noteGroupWithInfo = NoteGroupWithInfo()
                    .apply {
                        noteGroupWithInfoId = BaseTools.getUUID()
                        groupId = noteGroupInfo.noteGroupId
                        infoId = noteInfo.noteInfoId
                    }
                NoteGroupWithInfoService.insert(noteGroupWithInfo)
                val insertLink =
                    NoteGroupWithInfoService.findById(
                        noteGroupWithInfo.noteGroupWithInfoId
                    )
                if (insertLink != null) {
                    return true
                } else {
                    NoteGroupWithInfoService.delete(noteGroupWithInfo.noteGroupWithInfoId)
                }
            }
        }
        return false
    }

    /**
     * 通过组Id 查询全部 笔记
     */
    fun findAllNoteInfoByGroupId(groupId: String): List<NoteInfo> {
        val groupWidthInfoList = NoteGroupWithInfoService.findNoteInfoIdByGroupId(groupId)
        if (groupWidthInfoList.isNotEmpty()) {
            val noteInfoList = mutableListOf<NoteInfo>()
            for (groupWidthInfo in groupWidthInfoList) {
                val noteInfo = NoteInfoController.findById(groupWidthInfo.infoId)
                if (noteInfo != null) {
                    noteInfoList.add(noteInfo)
                }
            }
            return noteInfoList.sortedWith(object : Comparator<NoteInfo> {
                override fun compare(o1: NoteInfo?, o2: NoteInfo?): Int {
                    return if (o1 != null && o2 != null) {
                        if (o1.updateDate > o2.updateDate) {
                            -1
                        } else {
                            1
                        }
                    } else {
                        if (o1 == null) {
                            1
                        } else {
                            -1
                        }
                    }

                }
            })        }
        return emptyList()
    }


}