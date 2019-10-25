package com.inz.z.note_book.bean

import java.io.Serializable

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/25 17:00.
 */
class NoteInfo : Serializable {

    private var noteInfoId: Int? = null
    private var noteTitle = ""
    private var noteContent = ""
    private var createDate = ""
    private var updateDate = ""
    private var status = 0

    public enum class Status {
        /**
         * 未完成
         */
        UNFINISHED,
        /**
         * 已完成
         */
        FINISHED,
        /**
         * 已取消
         */
        CANCELED,
        /**
         * 已超时
         */
        TIMEOUT
    }

    /**
     * 设置状态
     */
    public fun setStatus(status: Status) {
        this.status =
            when (status) {
                Status.UNFINISHED -> 0
                Status.FINISHED -> 1
                Status.CANCELED -> -1
                Status.TIMEOUT -> -2
            }
    }

    /**
     * 获取状态
     */
    public fun getStatus(): Status {
        return when (status) {
            0 -> Status.UNFINISHED
            1 -> Status.FINISHED
            -1 -> Status.CANCELED
            -2 -> Status.TIMEOUT
            else -> Status.UNFINISHED
        }
    }
}
