package com.inz.z.music.view.adapter;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/04 17:58.
 */
public class ItemChooseInterestBean implements Serializable {
    private String id;
    /**
     * 连接
     */
    private String src = "";
    /**
     * 名称
     */
    private String name;
    /**
     * 详情
     */
    private String detail;
    /**
     * 是否被选中
     */
    private boolean isChose;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean isChose() {
        return isChose;
    }

    public void setChose(boolean chose) {
        isChose = chose;
    }

    @NonNull
    @Override
    public String toString() {
        return "ItemChooseInterestBean{" +
                "src='" + src + '\'' +
                ", name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", isChose=" + isChose +
                '}';
    }
}
