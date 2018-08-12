package com.inz.z_inz.presenter;

import com.inz.z_inz.base.BasePresenter;
import com.inz.z_inz.base.IBaseLoadListener;
import com.inz.z_inz.model.ILoginModel;
import com.inz.z_inz.model.LoginModelImpl;
import com.inz.z_inz.model.entity.BaseRequest;
import com.inz.z_inz.view.ILoginView;
import com.inz.z_inz.view.activity.MainActivity;

/**
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * @date Create By 2018/8/12 12:07
 */
public class LoginPresenter extends BasePresenter<ILoginView> {

    public void getData(String userName, String password){
        if (isViewAttached()) {
            // 如果没有View 引入，返回
            return;
        }
        // 显示加载弹窗
        getBaseView().showLoading();

        ILoginModel loginModel = new LoginModelImpl();
        loginModel.postLogin(userName, password, new IBaseLoadListener<BaseRequest>() {
            @Override
            public void loadStart() {
                if (isViewAttached()) {

                }
            }

            @Override
            public void loadSuccess(BaseRequest data) {
                if (isViewAttached()) {

                }
            }

            @Override
            public void loadFailure(int failureType, String msg) {
                if (isViewAttached()){
                    getBaseView().showToast(msg);
                }
            }

            @Override
            public void loadComplete() {
                if (isViewAttached()) {
                    getBaseView().hideLoading();
                }
            }
        });

    }
}
