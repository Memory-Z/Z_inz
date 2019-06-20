package com.inz.z.music.view.adapter;

import com.inz.z.music.database.SongsImageBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.DaoException;

import com.inz.z.music.database.DaoSession;
import com.inz.z.music.database.SongsImageBeanDao;
import com.inz.z.music.database.ItemSongsBeanDao;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/18 16:17.
 */
@Entity(nameInDb = "music_song", indexes = {
        @Index(value = "title ASC")
})
public class ItemSongsBean {

    @Id
    @Index(unique = true)
    @Property(nameInDb = "SONGS_ID")
    private String id = "0";

    private String title = "";
    private String detail = "";
    private boolean isDownload = false;
    private boolean isVip = false;
    private boolean haveVideo = false;
    private boolean haveHQ = false;
    private boolean haveSQ = false;
    private boolean isOnly = false;
    private long playNum = 0;
    private boolean isChecked = false;

    @ToMany(referencedJoinProperty = "imageId")
    private List<SongsImageBean> imageBeanList;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 320623401)
    private transient ItemSongsBeanDao myDao;

    @Generated(hash = 1988011301)
    public ItemSongsBean(String id, String title, String detail, boolean isDownload,
                         boolean isVip, boolean haveVideo, boolean haveHQ, boolean haveSQ,
                         boolean isOnly, long playNum, boolean isChecked) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.isDownload = isDownload;
        this.isVip = isVip;
        this.haveVideo = haveVideo;
        this.haveHQ = haveHQ;
        this.haveSQ = haveSQ;
        this.isOnly = isOnly;
        this.playNum = playNum;
        this.isChecked = isChecked;
    }

    @Generated(hash = 2106294463)
    public ItemSongsBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public boolean isHaveVideo() {
        return haveVideo;
    }

    public void setHaveVideo(boolean haveVideo) {
        this.haveVideo = haveVideo;
    }

    public boolean isHaveHQ() {
        return haveHQ;
    }

    public void setHaveHQ(boolean haveHQ) {
        this.haveHQ = haveHQ;
    }

    public boolean isHaveSQ() {
        return haveSQ;
    }

    public void setHaveSQ(boolean haveSQ) {
        this.haveSQ = haveSQ;
    }

    public boolean isOnly() {
        return isOnly;
    }

    public void setOnly(boolean only) {
        isOnly = only;
    }

    public long getPlayNum() {
        return playNum;
    }

    public void setPlayNum(long playNum) {
        this.playNum = playNum;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean getIsDownload() {
        return this.isDownload;
    }

    public void setIsDownload(boolean isDownload) {
        this.isDownload = isDownload;
    }

    public boolean getIsVip() {
        return this.isVip;
    }

    public void setIsVip(boolean isVip) {
        this.isVip = isVip;
    }

    public boolean getHaveVideo() {
        return this.haveVideo;
    }

    public boolean getHaveHQ() {
        return this.haveHQ;
    }

    public boolean getHaveSQ() {
        return this.haveSQ;
    }

    public boolean getIsOnly() {
        return this.isOnly;
    }

    public void setIsOnly(boolean isOnly) {
        this.isOnly = isOnly;
    }

    public boolean getIsChecked() {
        return this.isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1559991091)
    public List<SongsImageBean> getImageBeanList() {
        if (imageBeanList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SongsImageBeanDao targetDao = daoSession.getSongsImageBeanDao();
            List<SongsImageBean> imageBeanListNew = targetDao
                    ._queryItemSongsBean_ImageBeanList(id);
            synchronized (this) {
                if (imageBeanList == null) {
                    imageBeanList = imageBeanListNew;
                }
            }
        }
        return imageBeanList;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 658154040)
    public synchronized void resetImageBeanList() {
        imageBeanList = null;
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 149425692)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getItemSongsBeanDao() : null;
    }
}
