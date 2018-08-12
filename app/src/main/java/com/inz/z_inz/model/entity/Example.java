package com.inz.z_inz.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实例
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * @date Create By 2018/7/26 00:42
 */
public class Example implements Serializable {
    // 实例ID
    private int exampleId;
    // 实例名称
    private String exampleName;
    // 实例网络请求地址
    private String exampleImgUrl;
    // 实例本地请求地址
    private String exampleImgPath;
    // 实例更新时间
    private Date exampleUpdateDate;
    // 实例保存时间
    private Date exampleSaveDate;

    public int getExampleId() {
        return exampleId;
    }

    public void setExampleId(int exampleId) {
        this.exampleId = exampleId;
    }

    public String getExampleName() {
        return exampleName;
    }

    public void setExampleName(String exampleName) {
        this.exampleName = exampleName;
    }

    public String getExampleImgUrl() {
        return exampleImgUrl;
    }

    public void setExampleImgUrl(String exampleImgUrl) {
        this.exampleImgUrl = exampleImgUrl;
    }

    public String getExampleImgPath() {
        return exampleImgPath;
    }

    public void setExampleImgPath(String exampleImgPath) {
        this.exampleImgPath = exampleImgPath;
    }

    public Date getExampleUpdateDate() {
        return exampleUpdateDate;
    }

    public void setExampleUpdateDate(Date exampleUpdateDate) {
        this.exampleUpdateDate = exampleUpdateDate;
    }

    public Date getExampleSaveDate() {
        return exampleSaveDate;
    }

    public void setExampleSaveDate(Date exampleSaveDate) {
        this.exampleSaveDate = exampleSaveDate;
    }
}
