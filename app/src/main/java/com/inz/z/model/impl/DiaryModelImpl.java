package com.inz.z.model.impl;

import com.inz.z.base.IBaseLoadListener;
import com.inz.z.entity.ApiDiaryInfo;
import com.inz.z.http.HttpResponseFunction;
import com.inz.z.http.HttpUtil;
import com.inz.z.model.IDiaryModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/11/20 15:55.
 */
public class DiaryModelImpl implements IDiaryModel {
    @Override
    public void addDiaryInfo(String userId, String diaryContent, String diaryWeather, String diaryAddress, MultipartBody multipartBody, final IBaseLoadListener<ApiDiaryInfo> loadListener) {
        HttpUtil.addDiaryInfo(userId, diaryContent, diaryWeather, diaryAddress, multipartBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunction<ApiDiaryInfo>())
                .subscribe(new DisposableObserver<ApiDiaryInfo>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        loadListener.loadStart();
                    }

                    @Override
                    public void onNext(ApiDiaryInfo apiDiaryInfo) {
                        loadListener.loadSuccess(apiDiaryInfo);
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
