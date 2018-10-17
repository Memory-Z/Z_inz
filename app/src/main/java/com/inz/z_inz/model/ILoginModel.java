package com.inz.z_inz.model;

import com.inz.z_inz.base.IBaseLoadListener;
import com.inz.z_inz.base.IBaseModel;
import com.inz.z_inz.entity.ApiUserInfo;

/**
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create By 2018/8/12 14:45
 */
public interface ILoginModel extends IBaseModel {

    void postLogin(String userName, String password, IBaseLoadListener<ApiUserInfo> loadListener);
}
