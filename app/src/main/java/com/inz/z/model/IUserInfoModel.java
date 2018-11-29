package com.inz.z.model;

import com.inz.z.base.IBaseLoadListener;
import com.inz.z.entity.ApiDiaryInfo;
import com.inz.z.entity.ApiString;
import com.inz.z.entity.ApiUserInfo;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/11/20 20:44.
 */
public interface IUserInfoModel {

    /**
     * 注册用户
     */
    void registerUser(String userName, String password, IBaseLoadListener<ApiString> loadListener);

    /**
     * 发送注册邮箱验证码
     */
    void sendRegisterEmail(String userId, String userEmail, IBaseLoadListener<ApiString> loadListener);

    /**
     * 校验验证码
     */
    void checkRegisterCode(String userId, String emailCode, String userEmail, IBaseLoadListener<ApiString> loadListener);

    /**
     * 获取用户信息
     */
    void login(String userName, String password, IBaseLoadListener<ApiUserInfo> loadListener);
}
