package com.inz.z.music.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/19 15:55.
 */
@Entity(nameInDb = "songs_image")
public class SongsImageBean {
    @Id(autoincrement = true)
    private Long id = 0L;
    private String imageId;
    @NotNull
    @Unique
    private String songsId;
    private String imageSrc;
    private String imageUrl;
    private boolean imageIsDownload = false;
    private int imageWidth = 0;
    private int imageHeight = 0;
    private long imageSize = 0L;
    private String imageType;
    private String comment;

    @ToOne(joinProperty = "songsId")
    private ItemSongsBean songsBean;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 567633266)
    private transient SongsImageBeanDao myDao;

    @Generated(hash = 1512825656)
    public SongsImageBean(Long id, String imageId, @NotNull String songsId,
                          String imageSrc, String imageUrl, boolean imageIsDownload,
                          int imageWidth, int imageHeight, long imageSize, String imageType,
                          String comment) {
        this.id = id;
        this.imageId = imageId;
        this.songsId = songsId;
        this.imageSrc = imageSrc;
        this.imageUrl = imageUrl;
        this.imageIsDownload = imageIsDownload;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.imageSize = imageSize;
        this.imageType = imageType;
        this.comment = comment;
    }

    @Generated(hash = 744725184)
    public SongsImageBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageId() {
        return this.imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getSongsId() {
        return this.songsId;
    }

    public void setSongsId(String songsId) {
        this.songsId = songsId;
    }

    public String getImageSrc() {
        return this.imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean getImageIsDownload() {
        return this.imageIsDownload;
    }

    public void setImageIsDownload(boolean imageIsDownload) {
        this.imageIsDownload = imageIsDownload;
    }

    public int getImageWidth() {
        return this.imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return this.imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public long getImageSize() {
        return this.imageSize;
    }

    public void setImageSize(long imageSize) {
        this.imageSize = imageSize;
    }

    public String getImageType() {
        return this.imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Generated(hash = 1684558066)
    private transient String songsBean__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1753836252)
    public ItemSongsBean getSongsBean() {
        String __key = this.songsId;
        if (songsBean__resolvedKey == null || songsBean__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ItemSongsBeanDao targetDao = daoSession.getItemSongsBeanDao();
            ItemSongsBean songsBeanNew = targetDao.load(__key);
            synchronized (this) {
                songsBean = songsBeanNew;
                songsBean__resolvedKey = __key;
            }
        }
        return songsBean;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2125546431)
    public void setSongsBean(@NotNull ItemSongsBean songsBean) {
        if (songsBean == null) {
            throw new DaoException(
                    "To-one property 'songsId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.songsBean = songsBean;
            songsId = songsBean.getSongsId();
            songsBean__resolvedKey = songsId;
        }
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
    @Generated(hash = 2057286908)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSongsImageBeanDao() : null;
    }

}
