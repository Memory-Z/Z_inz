package com.inz.z_inz.http;

import com.inz.z_inz.entity.ApiUserInfo;
import com.inz.z_inz.entity.Constants;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络连接 请求数据
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create By 2018/8/12 14:24
 */
public class HttpUtil {

    /**
     * 默认连接超时时间，单位：秒
     */
    private static final int DEFAULT_TIMEOUT = 80;
    /**
     * 网络连接管理
     */
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .build();

    private static Retrofit retrofit;
    private static RetrofitInterface retrofitInterface;

    /**
     * 获取更新数据接口
     *
     * @return RetrofitInterface
     */
    private static synchronized RetrofitInterface getRetrofitInterface() {
        // 初始化 Retrofit 配置
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.getBaseUrl())
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            retrofitInterface = retrofit.create(RetrofitInterface.class);
        }
        return retrofitInterface;
    }

    /**
     * 用户登录接口
     *
     * @param userName 用户名
     * @param password 用户密码
     * @return 接口返回数据
     */
    public static Observable<ApiUserInfo> postLogin(String userName, String password) {
        return getRetrofitInterface().postLogin(userName, password);
    }

}
