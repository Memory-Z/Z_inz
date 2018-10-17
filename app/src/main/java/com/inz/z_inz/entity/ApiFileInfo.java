package com.inz.z_inz.entity;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * 接口文件信息
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/17 19:25.
 */
public class ApiFileInfo extends AbsRequestTemp1<FileInfo> implements Serializable {

    @NonNull
    @Override
    public String toString() {
        return "ApiFileInfo{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", tempType=" + tempType +
                ", data=" + data.toString() +
                '}';
    }
}
