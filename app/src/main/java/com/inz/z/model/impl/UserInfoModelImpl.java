package com.inz.z.model.impl;

import com.inz.z.base.IBaseLoadListener;
import com.inz.z.entity.ApiDiaryInfo;
import com.inz.z.entity.ApiString;
import com.inz.z.entity.ApiUserInfo;
import com.inz.z.http.HttpResponseFunction;
import com.inz.z.http.HttpUtil;
import com.inz.z.model.IUserInfoModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/11/20 20:45.
 */
public class UserInfoModelImpl implements IUserInfoModel {
    @Override
    public void registerUser(String userName, String password, final IBaseLoadListener<ApiString> loadListener) {
        HttpUtil.register(userName, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunction<ApiString>())
                .subscribe(new DisposableObserver<ApiString>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        loadListener.loadStart();
                    }

                    @Override
                    public void onNext(ApiString apiString) {
                        loadListener.loadSuccess(apiString);
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

    @Override
    public void sendRegisterEmail(String userId, String userEmail, final IBaseLoadListener<ApiString> loadListener) {
        HttpUtil.updateEmailCode(userId, userEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunction<ApiString>())
                .subscribe(new DisposableObserver<ApiString>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        loadListener.loadStart();
                    }

                    @Override
                    public void onNext(ApiString apiString) {
                        loadListener.loadSuccess(apiString);
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

    @Override
    public void checkRegisterCode(String userId, String emailCode, String userEmail, final IBaseLoadListener<ApiString> loadListener) {
        HttpUtil.sendUserEmailCode(userId, emailCode, userEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunction<ApiString>())
                .subscribe(new DisposableObserver<ApiString>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        loadListener.loadStart();
                    }

                    @Override
                    public void onNext(ApiString apiString) {
                        loadListener.loadSuccess(apiString);
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

    @Override
    public void login(String userName, String password, final IBaseLoadListener<ApiUserInfo> loadListener) {
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
