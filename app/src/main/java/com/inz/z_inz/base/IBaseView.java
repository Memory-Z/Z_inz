package com.inz.z_inz.base;

import android.content.Context;

/**
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * @date Create By 2018/8/12 10:16
 */
public interface IBaseView {

    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示提示
     *
     * @param msg 提示内容
     */
    void showToast(String msg);

    /**
     * 显示错误信息
     */
    void showError();

    /**
     * 获取上下文
     *
     * @return Context
     */
    Context getContext();
}

