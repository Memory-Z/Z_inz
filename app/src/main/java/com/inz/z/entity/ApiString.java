package com.inz.z.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * 字符串 Api
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/19 10:55.
 */
public class ApiString extends AbsRequestTemp1<String> implements Serializable {
    @NonNull
    @Override
    public String toString() {
        return "ApiString{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", tempType=" + tempType +
                ", data=" + data +
                '}';
    }
}
