package com.inz.z.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create By 2018/8/12 14:15
 */
public abstract class AbsRequestTemp1<T> implements Serializable {

    /**
     * 结果码
     */
    int code = 0;
    /**
     * 提示信息
     */
    String message = null;
    /**
     * 模板类型
     */
    int tempType = 0;
    /**
     * 数据内容
     */
    T data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTempType() {
        return tempType;
    }

    public void setTempType(int tempType) {
        this.tempType = tempType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @NonNull
    @Override
    public String toString() {
        return "AbsRequestTemp1{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", tempType=" + tempType +
                ", data=" + data.toString() +
                '}';
    }
}
