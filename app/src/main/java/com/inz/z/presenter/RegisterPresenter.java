package com.inz.z.presenter;

import com.inz.z.base.BasePresenter;
import com.inz.z.base.IBaseLoadListener;
import com.inz.z.entity.ApiString;
import com.inz.z.entity.ApiUserInfo;
import com.inz.z.model.IUserInfoModel;
import com.inz.z.model.impl.UserInfoModelImpl;
import com.inz.z.view.IRegisterView;

/**
 * 注册控制
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/11/20 20:50.
 */
public class RegisterPresenter extends BasePresenter<IRegisterView> {

    /**
     * 注册
     *
     * @param userName     用户名
     * @param userPassword 用户密码
     */
    public void register(String userName, String userPassword) {
        if (!isViewAttached()) {
            return;
        }
        IUserInfoModel userInfoModel = new UserInfoModelImpl();
        userInfoModel.registerUser(userName, userPassword, new IBaseLoadListener<ApiString>() {
            @Override
            public void loadStart() {
                if (isViewAttached()) {
                    getBaseView().showLoading();
                }
            }

            @Override
            public void loadSuccess(ApiString data) {
                if (isViewAttached()) {
                    String isRegisterStr = data.getData();
                    boolean isRegister = "true".equals(isRegisterStr);
                    getBaseView().setIsRegister(isRegister);
                    if (!isRegister) {
                        getBaseView().showToast(data.getMessage());
                    }
                }
            }

            @Override
            public void loadFailure(Throwable e) {
                if (isViewAttached()) {
                    getBaseView().showError(e);
                    getBaseView().hideLoading();
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

    /**
     * 注册试 登录
     *
     * @param userName     用户名
     * @param userPassword 密码
     */
    public void registerLogin(String userName, String userPassword) {
        if (!isViewAttached()) {
            return;
        }
        IUserInfoModel userInfoModel = new UserInfoModelImpl();
        userInfoModel.login(userName, userPassword, new IBaseLoadListener<ApiUserInfo>() {
            @Override
            public void loadStart() {
                if (isViewAttached()) {
                    getBaseView().showLoading();
                }
            }

            @Override
            public void loadSuccess(ApiUserInfo data) {
                if (isViewAttached()) {
                    String userId = data.getData().getUserId();
                    if (!"".equals(userId)) {
                        getBaseView().setRegisterUserId(userId);
                    }
                }
            }

            @Override
            public void loadFailure(Throwable e) {
                if (isViewAttached()) {
                    getBaseView().showError(e);
                    getBaseView().hideLoading();
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

    /**
     * 发送 注册验证码
     *
     * @param userId    用户ID
     * @param userEmail 用户邮箱
     */
    public void sendRegisterCode(String userId, String userEmail) {
        if (!isViewAttached()) {
            return;
        }
        IUserInfoModel userInfoModel = new UserInfoModelImpl();
        userInfoModel.sendRegisterEmail(userId, userEmail, new IBaseLoadListener<ApiString>() {
            @Override
            public void loadStart() {
                if (isViewAttached()) {
                    getBaseView().showToast("验证码发送中...");
                }
            }

            @Override
            public void loadSuccess(ApiString data) {
                if (isViewAttached()) {
                    String isSendStr = data.getData();
                    boolean isSend = "true".equals(isSendStr);
                    getBaseView().setIsSend(isSend);
                    getBaseView().showToast(data.getMessage());
                }
            }

            @Override
            public void loadFailure(Throwable e) {
                if (isViewAttached()) {
                    getBaseView().showError(e);
                }
            }

            @Override
            public void loadComplete() {
            }
        });
    }

    /**
     * 检测邮箱验证码
     *
     * @param userId    用户ID
     * @param emailCode 邮箱验证码
     * @param userEmail 用户邮箱
     */
    public void checkRegisterCode(String userId, String emailCode, String userEmail) {
        if (!isViewAttached()) {
            return;
        }
        IUserInfoModel userInfoModel = new UserInfoModelImpl();
        userInfoModel.checkRegisterCode(userId, emailCode, userEmail, new IBaseLoadListener<ApiString>() {
            @Override
            public void loadStart() {
                if (isViewAttached()) {
                    getBaseView().showLoading();
                }
            }

            @Override
            public void loadSuccess(ApiString data) {
                if (isViewAttached()) {
                    String checkStr = data.getData();
                    boolean isCheck = "true".equals(checkStr);
                    getBaseView().setCheckRegister(isCheck);
                    if (!isCheck) {
                        getBaseView().showToast(data.getMessage());
                    }
                }
            }

            @Override
            public void loadFailure(Throwable e) {
                if (isViewAttached()) {
                    getBaseView().showError(e);
                    getBaseView().hideLoading();
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
