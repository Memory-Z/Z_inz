package com.inz.z.view;

import com.inz.z.base.IBaseView;

/**
 * 注册视图
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/23 19:36.
 */
public interface IRegisterView extends IBaseView {

    void setIsRegister(boolean isRegister);

    void setIsSend(boolean isSend);

    /**
     * 获取注册用户ID
     *
     * @param userId 用户ID
     */
    void setRegisterUserId(String userId);

    void setCheckRegister(boolean checkRegister);

}
