package com.inz.z.music.view.adapter;

import java.io.Serializable;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/12 10:59.
 */
public class ItemTopPlayerBean implements Serializable {

    private String photoUrlStr;
    private String photoName;
    private MinStyle minPhotoStyle = MinStyle.NULL;

    public enum MinStyle {
        NULL(-1),
        MUSIC(0),
        RED_DOT(1);
        int style;

        MinStyle(int style) {
            this.style = style;
        }
    }

    public String getPhotoUrlStr() {
        return photoUrlStr;
    }

    public void setPhotoUrlStr(String photoUrlStr) {
        this.photoUrlStr = photoUrlStr;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public MinStyle getMinPhotoStyle() {
        return minPhotoStyle;
    }

    public void setMinPhotoStyle(MinStyle minPhotoStyle) {
        this.minPhotoStyle = minPhotoStyle;
    }
}
