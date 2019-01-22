package com.inz.z.http;

import com.inz.z.BuildConfig;
import com.inz.z.entity.ApiDiaryInfo;
import com.inz.z.entity.ApiFileInfo;
import com.inz.z.entity.ApiString;
import com.inz.z.entity.ApiUserInfo;
import com.inz.z.entity.Constants;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
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
    private static OkHttpClient client;

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new HttpLogInterceptor());
        }
        client = builder
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

    }

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

    /**
     * 用户注册
     *
     * @param userName 用户名
     * @param password 密码
     * @return 注册状态
     */
    public static Observable<ApiString> register(String userName, String password) {
        return getRetrofitInterface().register(userName, password);
    }

    /**
     * 更新用户邮箱
     *
     * @param userId    用户ID
     * @param userEmail 用户邮箱
     * @return 邮件验证码发送状态
     */
    public static Observable<ApiString> updateEmailCode(String userId, String userEmail) {
        return getRetrofitInterface().updateEmailCode(userId, userEmail);
    }

    /**
     * 验证验证码
     *
     * @param userId    用户Id
     * @param emailCode 邮箱验证码
     * @param userEmail 用户邮箱
     * @return 邮箱更新状态
     */
    public static Observable<ApiString> sendUserEmailCode(String userId, String emailCode, String userEmail) {
        return getRetrofitInterface().sendUserEmailCode(userId, emailCode, userEmail);
    }

    /**
     * 更新用户头像
     *
     * @param userId 用户ID
     * @param photo  头像文件
     * @return 状态信息
     */
    public static Observable<ApiString> updateUserPhoto(String userId, MultipartBody.Part photo) {
        return getRetrofitInterface().updateUserPhoto(userId, photo);
    }

    /**
     * 更新用户信息
     *
     * @param userId       用户ID
     * @param userSex      用户性别： 1：男，0：女
     * @param birthdayDate 生日
     * @param userIntro    用户简介
     * @param userMemo     用户备注
     * @param userPhone    用户手机号
     * @return 用户信息
     */
    public static Observable<ApiUserInfo> updateUserInfo(String userId, String userSex,
                                                         String birthdayDate, String userIntro,
                                                         String userMemo, String userPhone) {
        return getRetrofitInterface().updateUserInfo(userId, userSex, birthdayDate, userIntro, userMemo, userPhone);
    }

    /**
     * 修改用户密码
     *
     * @param userId       用户ID
     * @param userPassword 用户新密码
     * @return 修改状态
     */
    public static Observable<ApiString> changePassword(String userId, String userPassword) {
        return getRetrofitInterface().changePassword(userId, userPassword);
    }

    /**
     * 获取日志信息
     *
     * @param userId   用户ID
     * @param start    开始页数
     * @param pageSize 页面大小
     * @return 日志信息
     */
    public static Observable<List<ApiDiaryInfo>> getDiaryList(String userId, String start, String pageSize) {
        return getRetrofitInterface().getDiaryList(userId, start, pageSize);
    }

    /**
     * 获取用户某一日志的文件信息
     *
     * @param userId  用户ID
     * @param diaryId 日志ID
     * @return 日志文件信息
     */
    public static Observable<List<ApiFileInfo>> getDiaryFileList(String userId, String diaryId) {
        return getRetrofitInterface().getDiaryFileList(userId, diaryId);
    }

    /**
     * 日志添加
     *
     * @param userId       用户ID
     * @param diaryContent 日志内容
     * @param diaryWeather 日志天气
     * @param diaryAddress 日志地址
     * @param diaryPhoto   日志图片
     * @return 日志信息
     */
    public static Observable<ApiDiaryInfo> addDiaryInfo(String userId, String diaryContent,
                                                        String diaryWeather, String diaryAddress,
                                                        MultipartBody diaryPhoto) {
        return getRetrofitInterface().addDiaryInfo(userId, diaryContent, diaryWeather, diaryAddress, diaryPhoto);
    }

    /**
     * 日志删除
     *
     * @param userId  用户Id
     * @param diaryId 日志Id
     * @return 日志删除状态
     */
    public static Observable<ApiString> deleteDiaryInfo(String userId, String diaryId) {
        return getRetrofitInterface().deleteDiaryInfo(userId, diaryId);
    }

}
