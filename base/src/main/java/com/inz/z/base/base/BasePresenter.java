package com.inz.z.base.base;

/**
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * @date Create By 2018/8/12 10:33
 */
public class BasePresenter<V extends IBaseView> {

    private V baseView;

    /**
     * 绑定 View, 初始化时调用
     *
     * @param baseView 视图
     */
    public void attachView(V baseView) {
        this.baseView = baseView;
    }

    /**
     * 断开 View, 一般在 onDestroy 中调用
     */
    public void detachView() {
        this.baseView = null;
    }

    /**
     * 是否与 View 建立连接，每次调用业务请求的时候都要出先调用方法检查是否与View建立连接
     *
     * @return 是否建立连接
     */
    public boolean isViewAttached() {
        return baseView != null;
    }

    /**
     * 获取连接的View
     *
     * @return View
     */
    public V getBaseView() {
        return baseView;
    }
}
