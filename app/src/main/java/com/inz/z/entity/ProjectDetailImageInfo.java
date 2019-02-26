package com.inz.z.entity;

import org.jetbrains.annotations.NotNull;

/**
 * 产品详情 图片名称
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/1/27 15:47.
 */
public class ProjectDetailImageInfo {
    /**
     * 图片地址
     */
    private String imageUrl;
    /**
     * 图片标题
     */
    private String imageTitle;
    /**
     * 图片提示内容
     */
    private String imageHintStr;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getImageHintStr() {
        return imageHintStr;
    }

    public void setImageHintStr(String imageHintStr) {
        this.imageHintStr = imageHintStr;
    }

    @NotNull
    @Override
    public String toString() {
        return "ProjectDetailImageInfo{" +
                "imageUrl='" + imageUrl + '\'' +
                ", imageTitle='" + imageTitle + '\'' +
                ", imageHintStr='" + imageHintStr + '\'' +
                '}';
    }
}
