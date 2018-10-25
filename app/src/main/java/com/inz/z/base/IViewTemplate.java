package com.inz.z.base;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/23 18:55.
 */
public interface IViewTemplate extends IBaseView {
    /**
     * 初始化视图
     */
    void initView();

    /**
     * 初始化数据
     */
    void initData();
}
