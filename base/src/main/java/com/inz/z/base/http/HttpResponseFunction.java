package com.inz.z.base.http;

import com.orhanobut.logger.Logger;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Http 返回数据拦截器
 * Create by inz
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by 2018/9/20 14:07.
 */
public class HttpResponseFunction<T> implements Function<Throwable, Observable<T>> {
    private static final String TAG = "HttpResponseFunction";

    @Override
    public Observable<T> apply(Throwable throwable) {
        Logger.t(TAG).e(throwable, "Http 请求出错！");
        return Observable.error(ExceptionHandler.handleException(throwable));
    }
}
