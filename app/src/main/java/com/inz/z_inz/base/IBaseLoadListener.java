package com.inz.z_inz.base;

/**
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * @date Create By 2018/8/12 11:47
 */
public interface IBaseLoadListener<T> {

    /**
     * 开始加载
     */
    void loadStart();

    /**
     * 加载成功
     */
    void loadSuccess(T data);

    /**
     * 加载失败
     *
     * @param failureType 失败类型
     * @param msg         失败内容
     */
    void loadFailure(int failureType, String msg);

    /**
     * 加载完成
     */
    void loadComplete();

}
