package com.inz.choosefile.bean;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * 时评数据
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/28 10:31.
 */
public class VideoBean extends FileBean implements Serializable {
    private String name;
    private String path;
    private String mediaType;
    private long size;
    private long duration;
    private boolean isVideoChecked = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isVideoChecked() {
        return isVideoChecked;
    }

    public void setVideoChecked(boolean videoChecked) {
        isVideoChecked = videoChecked;
    }

    @NonNull
    @Override
    public String toString() {
        return "VideoBean{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", size=" + size +
                ", duration=" + duration +
                ", isVideoChecked=" + isVideoChecked +
                '}';
    }
}
