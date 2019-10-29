package com.inz.z.note_book.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.security.Timestamp;
import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.DaoException;
import com.inz.z.note_book.database.DaoSession;
import com.inz.z.note_book.database.NoteInfoDao;
import com.inz.z.note_book.database.NoteGroupDao;

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

    @ToMany(referencedJoinProperty = "noteInfoId")
    private List<NoteInfo> noteInfoList;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 825967275)
    private transient NoteGroupDao myDao;

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

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 557458481)
    public List<NoteInfo> getNoteInfoList() {
        if (noteInfoList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            NoteInfoDao targetDao = daoSession.getNoteInfoDao();
            List<NoteInfo> noteInfoListNew = targetDao
                    ._queryNoteGroup_NoteInfoList(noteGroupId);
            synchronized (this) {
                if (noteInfoList == null) {
                    noteInfoList = noteInfoListNew;
                }
            }
        }
        return noteInfoList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 523117242)
    public synchronized void resetNoteInfoList() {
        noteInfoList = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 885813466)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getNoteGroupDao() : null;
    }

}
