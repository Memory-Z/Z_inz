package com.inz.z.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * 接口用户信息
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/17 18:59.
 */
public class ApiUserInfo extends AbsRequestTemp1<UserInfo> implements Serializable {

    @NonNull
    @Override
    public String toString() {
        return "ApiUserInfo{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", tempType=" + tempType +
                ", data=" + data.toString() +
                '}';
    }
}
