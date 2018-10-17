package com.inz.z_inz.http;

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
    @Override
    public Observable<T> apply(Throwable throwable) {
        return Observable.error(ExceptionHandler.handleException(throwable));
    }
}
