package com.inz.z.music.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/26 16:02.
 */
@Entity(nameInDb = "songs_folder")
public class SongsFolderBean {
    @Id
    @NotNull
    private String songsFolderId;
    @NotNull
    private String folderName;

    private boolean isChecked = false;

    @ToMany(referencedJoinProperty = "folderId")
    private List<ItemSongsBean> itemSongsBeanList;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1797780949)
    private transient SongsFolderBeanDao myDao;

    @Generated(hash = 1210852809)
    public SongsFolderBean(@NotNull String songsFolderId,
            @NotNull String folderName, boolean isChecked) {
        this.songsFolderId = songsFolderId;
        this.folderName = folderName;
        this.isChecked = isChecked;
    }

    @Generated(hash = 2107762457)
    public SongsFolderBean() {
    }

    public String getSongsFolderId() {
        return this.songsFolderId;
    }

    public void setSongsFolderId(String songsFolderId) {
        this.songsFolderId = songsFolderId;
    }

    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
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
    @Generated(hash = 1020655178)
    public List<ItemSongsBean> getItemSongsBeanList() {
        if (itemSongsBeanList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ItemSongsBeanDao targetDao = daoSession.getItemSongsBeanDao();
            List<ItemSongsBean> itemSongsBeanListNew = targetDao
                    ._querySongsFolderBean_ItemSongsBeanList(songsFolderId);
            synchronized (this) {
                if (itemSongsBeanList == null) {
                    itemSongsBeanList = itemSongsBeanListNew;
                }
            }
        }
        return itemSongsBeanList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 278463717)
    public synchronized void resetItemSongsBeanList() {
        itemSongsBeanList = null;
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
    @Generated(hash = 1489128555)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSongsFolderBeanDao() : null;
    }

}
