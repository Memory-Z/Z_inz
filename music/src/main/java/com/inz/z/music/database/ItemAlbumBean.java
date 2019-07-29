package com.inz.z.music.database;

import android.support.annotation.NonNull;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/14 15:28.
 */
public class ItemAlbumBean {
    private String albumSrc;
    private String titleName;
    private String playerName;
    private boolean isNew = false;
    private boolean isHot = false;

    public String getAlbumSrc() {
        return albumSrc;
    }

    public void setAlbumSrc(String albumSrc) {
        this.albumSrc = albumSrc;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    @NonNull
    @Override
    public String toString() {
        return "ItemAlbumBean{" +
                "albumSrc='" + albumSrc + '\'' +
                ", titleName='" + titleName + '\'' +
                ", playerName='" + playerName + '\'' +
                ", isNew=" + isNew +
                ", isHot=" + isHot +
                '}';
    }
}
