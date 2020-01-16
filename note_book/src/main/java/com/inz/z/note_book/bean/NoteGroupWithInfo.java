package com.inz.z.note_book.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 组与信息关联
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/28 14:45.
 */
@Entity(nameInDb = "rel_note_group_info")
public class NoteGroupWithInfo {

    /**
     * Id
     */
    @Id
    private String noteGroupWithInfoId = "";
    /**
     * 组ID
     */
    private String groupId = "";
    /**
     * 信息Id
     */
    private String infoId = "";
    @Generated(hash = 1929991068)
    public NoteGroupWithInfo(String noteGroupWithInfoId, String groupId,
            String infoId) {
        this.noteGroupWithInfoId = noteGroupWithInfoId;
        this.groupId = groupId;
        this.infoId = infoId;
    }
    @Generated(hash = 256042837)
    public NoteGroupWithInfo() {
    }
    public String getNoteGroupWithInfoId() {
        return this.noteGroupWithInfoId;
    }
    public void setNoteGroupWithInfoId(String noteGroupWithInfoId) {
        this.noteGroupWithInfoId = noteGroupWithInfoId;
    }
    public String getGroupId() {
        return this.groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getInfoId() {
        return this.infoId;
    }
    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }
}
