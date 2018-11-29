package com.inz.z.model;

import com.inz.z.base.IBaseLoadListener;
import com.inz.z.entity.ApiUserInfo;

/**
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create By 2018/8/12 14:45
 */
public interface ILoginModel {

    void postLogin(String userName, String password, IBaseLoadListener<ApiUserInfo> loadListener);
}
