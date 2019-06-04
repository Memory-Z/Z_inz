package com.inz.choosefile.bean;

import java.io.Serializable;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/26 15:20.
 */
public class ImageBean implements Serializable {
    private String path;
    private String name;
    private boolean isChecked = false;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "ImageBean{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}

