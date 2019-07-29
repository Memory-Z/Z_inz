package com.inz.z.music.database;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

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
    private String songsId = "0";
    private String displayName = "";
    private String title = "";
    private String album = "";
    private String artist = "";
    private String filePath = "";
    private String mimeType = "";
    private long size = 0L;
    private long duration = 0L;
    private String createDate = "";
    private String updateDate = "";
    private String folderId = "";
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
/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;
/** Used for active entity operations. */
@Generated(hash = 320623401)
private transient ItemSongsBeanDao myDao;

@Generated(hash = 2129163194)
public ItemSongsBean(String songsId, String displayName, String title,
        String album, String artist, String filePath, String mimeType,
        long size, long duration, String createDate, String updateDate,
        String folderId, boolean isDownload, boolean isVip, boolean haveVideo,
        boolean haveHQ, boolean haveSQ, boolean isOnly, long playNum,
        boolean isChecked) {
    this.songsId = songsId;
    this.displayName = displayName;
    this.title = title;
    this.album = album;
    this.artist = artist;
    this.filePath = filePath;
    this.mimeType = mimeType;
    this.size = size;
    this.duration = duration;
    this.createDate = createDate;
    this.updateDate = updateDate;
    this.folderId = folderId;
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

public String getSongsId() {
    return this.songsId;
}

public void setSongsId(String songsId) {
    this.songsId = songsId;
}

public String getDisplayName() {
    return this.displayName;
}

public void setDisplayName(String displayName) {
    this.displayName = displayName;
}

public String getTitle() {
    return this.title;
}

public void setTitle(String title) {
    this.title = title;
}

public String getAlbum() {
    return this.album;
}

public void setAlbum(String album) {
    this.album = album;
}

public String getArtist() {
    return this.artist;
}

public void setArtist(String artist) {
    this.artist = artist;
}

public String getFilePath() {
    return this.filePath;
}

public void setFilePath(String filePath) {
    this.filePath = filePath;
}

public String getMimeType() {
    return this.mimeType;
}

public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
}

public long getSize() {
    return this.size;
}

public void setSize(long size) {
    this.size = size;
}

public long getDuration() {
    return this.duration;
}

public void setDuration(long duration) {
    this.duration = duration;
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

public String getFolderId() {
    return this.folderId;
}

public void setFolderId(String folderId) {
    this.folderId = folderId;
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

public void setHaveVideo(boolean haveVideo) {
    this.haveVideo = haveVideo;
}

public boolean getHaveHQ() {
    return this.haveHQ;
}

public void setHaveHQ(boolean haveHQ) {
    this.haveHQ = haveHQ;
}

public boolean getHaveSQ() {
    return this.haveSQ;
}

public void setHaveSQ(boolean haveSQ) {
    this.haveSQ = haveSQ;
}

public boolean getIsOnly() {
    return this.isOnly;
}

public void setIsOnly(boolean isOnly) {
    this.isOnly = isOnly;
}

public long getPlayNum() {
    return this.playNum;
}

public void setPlayNum(long playNum) {
    this.playNum = playNum;
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
@Generated(hash = 1300473647)
public List<SongsImageBean> getImageBeanList() {
    if (imageBeanList == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        SongsImageBeanDao targetDao = daoSession.getSongsImageBeanDao();
        List<SongsImageBean> imageBeanListNew = targetDao
                ._queryItemSongsBean_ImageBeanList(songsId);
        synchronized (this) {
            if (imageBeanList == null) {
                imageBeanList = imageBeanListNew;
            }
        }
    }
    return imageBeanList;
}

/** Resets a to-many relationship, making the next get call to query for a fresh result. */
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

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 149425692)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getItemSongsBeanDao() : null;
}



}
