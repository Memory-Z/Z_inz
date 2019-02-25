package com.inz.z.model.impl;

import com.inz.z.base.IBaseLoadListener;
import com.inz.z.entity.ApiUserInfo;
import com.inz.z.entity.UserInfo;
import com.inz.z.http.HttpResponseFunction;
import com.inz.z.http.HttpUtil;
import com.inz.z.model.ILoginModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create By 2018/8/12 14:47
 */
public class LoginModelImpl implements ILoginModel {

    @Override
    public void postLogin(String userName, String password, final IBaseLoadListener<ApiUserInfo> loadListener) {
        HttpUtil.postLogin(userName, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunction<ApiUserInfo>())
                .subscribe(new DisposableObserver<ApiUserInfo>() {

                    @Override
                    protected void onStart() {
                        super.onStart();
                        loadListener.loadStart();
                    }

                    @Override
                    public void onNext(ApiUserInfo apiUserInfo) {
                        loadListener.loadSuccess(apiUserInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadListener.loadFailure(e);
                    }

                    @Override
                    public void onComplete() {
                        loadListener.loadComplete();
                    }
                });

    }
}
