package com.inz.z_inz.util;

import com.inz.z_inz.model.entity.BaseRequest;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * @date Create By 2018/8/12 14:06
 */
public interface RetrofitInterface {

    /**
     * 用户登录
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    @POST("dataForAppController.do?login")
    Observable<BaseRequest> postLogin(@Query("userName") String userName,
                                      @Query("password") String password);
}
