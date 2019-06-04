package com.inz.z.http;

import com.inz.z.BuildConfig;
import com.inz.z.base.http.BaseHttpUtil;
import com.inz.z.entity.ApiDiaryInfo;
import com.inz.z.entity.ApiFileInfo;
import com.inz.z.entity.ApiString;
import com.inz.z.entity.ApiUserInfo;
import com.inz.z.entity.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
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
public class HttpUtil extends BaseHttpUtil {

    /**
     * 默认连接超时时间，单位：秒
     */
    private static final int DEFAULT_TIMEOUT = 10;
    /**
     * 网络连接管理
     */
    private static OkHttpClient client;
    private final static MediaType textType = MediaType.parse("text/plain");

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
     * 文件 传输 网络接口
     */
    private static class FileRetrofitInterfaceBuilder {
        /**
         * 上传进度监听
         */
        private ProgressRequestListener progressRequestListener;

        /**
         * 下载进度监听
         */
        private ProgressResponseListener progressResponseListener;
        private OkHttpClient.Builder builder;

        // 文件超时时间
        private long fileTimeOut = 2;

        FileRetrofitInterfaceBuilder() {
            builder = new OkHttpClient.Builder();
        }

        /**
         * 添加 上传进度监听 {@link #setProgressResponseListener}
         *
         * @param progressRequestListener 上传监听
         * @return Builder
         */
        FileRetrofitInterfaceBuilder setProgressRequestListener(ProgressRequestListener progressRequestListener) {
            this.progressRequestListener = progressRequestListener;
            return this;
        }

        /**
         * 添加 下载进度监听 {@link #setProgressRequestListener}
         *
         * @param progressResponseListener 下载监听
         * @return Builder
         */
        FileRetrofitInterfaceBuilder setProgressResponseListener(ProgressResponseListener progressResponseListener) {

            this.progressResponseListener = progressResponseListener;
            return this;
        }

        /**
         * 设置文件超时时间
         *
         * @param fileTimeOut 超时时间 单位：分
         * @return Builder
         */
        FileRetrofitInterfaceBuilder setFileTimeOut(long fileTimeOut) {
            this.fileTimeOut = fileTimeOut;
            return this;
        }

        RetrofitInterface build() {
            // 上传
            if (progressRequestListener != null) {
                builder.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .method(original.method(), new ProgressRequestBody(original.body(), progressRequestListener))
                                .build();
                        return chain.proceed(request);
                    }
                });
            }
            // 下载
            if (progressResponseListener != null) {
                builder.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Response original = chain.proceed(chain.request());
                        return original.newBuilder()
                                .body(new ProgressResponseBody(original.body(), progressResponseListener))
                                .build();
                    }
                });
            }
            // 请求区
            OkHttpClient client = builder.connectTimeout(fileTimeOut, TimeUnit.MINUTES)
                    .readTimeout(fileTimeOut, TimeUnit.MINUTES)
                    .writeTimeout(fileTimeOut, TimeUnit.MINUTES)
                    .build();
            // Retrofit
            Retrofit fileRetrofit = new Retrofit.Builder()
                    .baseUrl(Constants.getBaseUrl())
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            return fileRetrofit.create(RetrofitInterface.class);
        }
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
     * @param userId    用户ID
     * @param photoFile 头像文件
     * @return 状态信息
     */
    public static Observable<ApiString> updateUserPhoto(String userId, File photoFile, ProgressRequestListener listener) {
//        return getRetrofitInterface().updateUserPhoto(userId, photo);
        RequestBody photoBody = RequestBody.create(MediaType.parse("image/*"), photoFile);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", photoFile.getName(), photoBody);
        return new FileRetrofitInterfaceBuilder()
                .setFileTimeOut(2)
                .setProgressRequestListener(listener)
                .build()
                .updateUserPhoto(userId, photo);
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
     * @param files        日志图片集
     * @return 日志信息
     */
    public static Observable<ApiDiaryInfo> addDiaryInfo(String userId, String diaryContent,
                                                        String diaryWeather, String diaryAddress,
                                                        File[] files,
                                                        ProgressRequestListener listener) {
        Map<String, RequestBody> partMap = new HashMap<>();
        RequestBody contentBody = RequestBody.create(HttpUtil.textType, diaryContent);
        RequestBody weatherBody = RequestBody.create(HttpUtil.textType, diaryWeather);
        RequestBody addressBody = RequestBody.create(HttpUtil.textType, diaryAddress);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (File file : files) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part diaryPhoto = MultipartBody.Part.createFormData("photo", file.getName(), requestBody);
            builder.addPart(diaryPhoto);
        }
        MultipartBody photoBody = builder.build();
        partMap.put("diaryContent", contentBody);
        partMap.put("diaryWeather", weatherBody);
        partMap.put("diaryAddress", addressBody);
        return new FileRetrofitInterfaceBuilder()
                .setFileTimeOut(10)
                .setProgressRequestListener(listener)
                .build()
                .addDiaryInfo(userId, partMap, photoBody);
//        return getRetrofitInterface().addDiaryInfo(userId, diaryContent, diaryWeather, diaryAddress, diaryPhoto);
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
