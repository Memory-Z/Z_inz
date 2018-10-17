package com.inz.z_inz.presenter;

import com.inz.z_inz.base.BasePresenter;
import com.inz.z_inz.base.IBaseLoadListener;
import com.inz.z_inz.entity.ApiUserInfo;
import com.inz.z_inz.model.ILoginModel;
import com.inz.z_inz.model.impl.LoginModelImpl;
import com.inz.z_inz.entity.AbsRequestTemp1;
import com.inz.z_inz.view.ILoginView;

/**
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * @date Create By 2018/8/12 12:07
 */
public class LoginPresenter extends BasePresenter<ILoginView> {

    public void getData(String userName, String password) {
        if (isViewAttached()) {
            // 如果没有View 引入，返回
            return;
        }
        // 显示加载弹窗
        getBaseView().showLoading();

        ILoginModel loginModel = new LoginModelImpl();
        loginModel.postLogin(userName, password, new IBaseLoadListener<ApiUserInfo>() {
            @Override
            public void loadStart() {

            }

            @Override
            public void loadSuccess(ApiUserInfo data) {

            }

            @Override
            public void loadFailure(int failureType, String msg) {

            }

            @Override
            public void loadComplete() {

            }
        });

    }
}
