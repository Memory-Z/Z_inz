package com.inz.z.base.http;

import com.inz.z.base.BuildConfig;
import com.inz.z.base.entity.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
public abstract class BaseHttpUtil {

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

}
