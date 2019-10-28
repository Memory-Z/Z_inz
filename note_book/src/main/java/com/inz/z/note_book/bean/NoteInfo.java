package com.inz.z.note_book.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/28 14:28.
 */
@Entity(nameInDb = "note_info")
public class NoteInfo {

    /**
     * id
     */
    @Id
    private String noteInfoId = "";
    /**
     * 标题
     */
    private String noteTitle = "";
    /**
     * 内容
     */
    private String noteContent = "";
    /**
     * 创建时间
     */
    private String createDate = "";
    /**
     * 更新时间
     */
    private String updateDate = "";
    /**
     * 状态
     *
     * @see Status 状态
     */
    private int status = 0;

    @Generated(hash = 1537587588)
    public NoteInfo(String noteInfoId, String noteTitle, String noteContent,
            String createDate, String updateDate, int status) {
        this.noteInfoId = noteInfoId;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.status = status;
    }

    @Generated(hash = 1097220926)
    public NoteInfo() {
    }

    /**
     * 状态
     */
    public enum Status {
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
    public void setNoteStatus(Status status) {
        if (status == Status.UNFINISHED) {
            this.status = 0;
        }
        if (status == Status.FINISHED) {
            this.status = 1;
        }
        if (status == Status.CANCELED) {
            this.status = -1;
        }
        if (status == Status.TIMEOUT) {
            this.status = -2;
        }
    }

    /**
     * 获取状态
     */
    public Status getNoteStatus() {
        Status status = Status.UNFINISHED;
        switch (this.status) {
            case 0:
                status = Status.UNFINISHED;
                break;
            case 1:
                status = Status.FINISHED;
                break;
            case -1:
                status = Status.CANCELED;
                break;
            case -2:
                status = Status.TIMEOUT;
                break;
        }
        return status;
    }

    public String getNoteInfoId() {
        return this.noteInfoId;
    }

    public void setNoteInfoId(String noteInfoId) {
        this.noteInfoId = noteInfoId;
    }

    public String getNoteTitle() {
        return this.noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return this.noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }
}
