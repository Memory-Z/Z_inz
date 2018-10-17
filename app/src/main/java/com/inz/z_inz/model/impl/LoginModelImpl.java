package com.inz.z_inz.model.impl;

import com.inz.z_inz.base.IBaseLoadListener;
import com.inz.z_inz.entity.ApiUserInfo;
import com.inz.z_inz.entity.UserInfo;
import com.inz.z_inz.http.HttpResponseFunction;
import com.inz.z_inz.http.HttpUtil;
import com.inz.z_inz.model.ILoginModel;

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
    public void postLogin(String userName, String password, IBaseLoadListener<ApiUserInfo> loadListener) {
        HttpUtil.postLogin(userName, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunction<ApiUserInfo>())
                .subscribe(new DisposableObserver<ApiUserInfo>() {

                    @Override
                    protected void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onNext(ApiUserInfo apiUserInfo) {
                        UserInfo userInfo = apiUserInfo.getData();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
