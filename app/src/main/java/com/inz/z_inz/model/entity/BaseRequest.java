package com.inz.z_inz.model.entity;

/**
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * @date Create By 2018/8/12 14:15
 */
public class BaseRequest {

    /**
     * 返回状态码
     */
    public String code;
    /**
     * 返回的数据
     */
    public String data;
    /**
     * 返回的信息
     */
    public String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
