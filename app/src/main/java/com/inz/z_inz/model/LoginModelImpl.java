package com.inz.z_inz.model;

import com.inz.z_inz.base.IBaseLoadListener;
import com.inz.z_inz.model.entity.BaseRequest;
import com.inz.z_inz.util.RequestDataUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * @date Create By 2018/8/12 14:47
 */
public class LoginModelImpl implements ILoginModel {

    @Override
    public void postLogin(String userName, String password, IBaseLoadListener loadListener) {
        RequestDataUtil.postLogin(userName, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<BaseRequest>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onNext(BaseRequest baseRequest) {

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
