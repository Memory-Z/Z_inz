package com.inz.z_inz.entity;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * 日志信息接口
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/17 20:05.
 */
public class ApiDiaryInfo extends AbsRequestTemp1<DiaryInfo> implements Serializable {
    @NonNull
    @Override
    public String toString() {
        return "ApiDiaryInfo{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", tempType=" + tempType +
                ", data=" + data.toString() +
                '}';
    }
}
