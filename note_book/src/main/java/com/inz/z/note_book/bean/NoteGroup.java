package com.inz.z.note_book.bean;

import com.inz.z.note_book.database.DaoSession;
import com.inz.z.note_book.database.NoteGroupDao;
import com.inz.z.note_book.database.NoteInfoDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.Date;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/28 14:50.
 */
@Entity(nameInDb = "note_group")
public class NoteGroup {
    /**
     * id
     */
    @Id
    private String noteGroupId = "";
    /**
     * 组名
     */
    private String groupName = "";
    /**
     * 优先级 【1 > 2 > 3 > 4 】 defalut : 4
     */
    private int priority = 4;

    /**
     * 是否收藏 ；0 : 未收藏；1 ： 已收藏
     */
    private int isCollectValue = 0;
    /**
     * 排序
     */
    private int order = 0;
    /**
     * 创建时间
     */
    private Date createDate = null;
    /**
     * 更新时间
     */
    private Date updateDate = null;

    @Generated(hash = 2061129399)
    public NoteGroup(String noteGroupId, String groupName, int priority,
            int isCollectValue, int order, Date createDate, Date updateDate) {
        this.noteGroupId = noteGroupId;
        this.groupName = groupName;
        this.priority = priority;
        this.isCollectValue = isCollectValue;
        this.order = order;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    @Generated(hash = 395212008)
    public NoteGroup() {
    }

    public String getNoteGroupId() {
        return this.noteGroupId;
    }

    public void setNoteGroupId(String noteGroupId) {
        this.noteGroupId = noteGroupId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getIsCollectValue() {
        return this.isCollectValue;
    }

    public void setIsCollectValue(int isCollectValue) {
        this.isCollectValue = isCollectValue;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}
