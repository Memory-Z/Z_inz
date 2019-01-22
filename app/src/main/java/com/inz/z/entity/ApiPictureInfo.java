package com.inz.z.entity;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * 图片信息接口
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/17 20:02.
 */
public class ApiPictureInfo extends AbsRequestTemp1<ApiPictureInfo> implements Serializable {

    @NonNull
    @Override
    public String toString() {
        return "ApiPictureInfo{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", tempType=" + tempType +
                ", data=" + data.toString() +
                '}';
    }
}
