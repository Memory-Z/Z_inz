package com.inz.z_inz.http;

import com.inz.z_inz.entity.ApiUserInfo;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 网络请求地址
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create By 2018/8/12 14:06
 */
public interface RetrofitInterface {

    /**
     * 用户登录
     *
     * @param userName 用户名
     * @param password 密码
     * @return 用户信息
     */
    @POST("login")
    Observable<ApiUserInfo> postLogin(@Query("userName") String userName,
                                      @Query("password") String password);
}
