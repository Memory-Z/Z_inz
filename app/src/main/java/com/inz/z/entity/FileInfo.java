package com.inz.z.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 文件信息
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/17 19:25.
 */
public class FileInfo implements Serializable {
    /**
     * 文件ID
     */
    private String fileId;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件链接
     */
    private String fileUrl;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件大小
     */
    private BigDecimal fileSize;
    /**
     * 是否可用
     */
    private boolean fileEnable;
    /**
     * 文件创建时间
     */
    private String createDatetime;
    /**
     * 文件更新时间
     */
    private String updateTime;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public BigDecimal getFileSize() {
        return fileSize;
    }

    public void setFileSize(BigDecimal fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isFileEnable() {
        return fileEnable;
    }

    public void setFileEnable(boolean fileEnable) {
        this.fileEnable = fileEnable;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "fileId='" + fileId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                ", fileEnable=" + fileEnable +
                ", createDatetime='" + createDatetime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
