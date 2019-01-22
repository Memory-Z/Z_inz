package com.inz.z;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.inz.z.entity.Constants;
import com.inz.z.util.CrashHandler;
import com.inz.z.util.MyCsvFormatStrategy;
import com.inz.z.util.SPHelper;
import com.inz.z.util.Tools;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/23 19:06.
 */
public class InzApplication extends Application {
    private static final String TAG = "InzApplication";
    private Context mContext;

    public InzApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: " + System.currentTimeMillis());
        mContext = this;
        // 初始化 日志
        initLogger();
        // 非 测试环境
        if (!Constants.isIsTest()) {
            // 初始化 异常信息收集
            initCrash();
        }
        // 注册 Activity 生命周期 回调 函数
        registerActivityLifecycleCallbacks(new InzActivityLifecycleCallbacks());
        // 初始化 SharePreferences
        SPHelper.getInstance().initSharedPreferences(mContext);
        Log.i(TAG, "onCreate: " + System.currentTimeMillis());
    }

    /**
     * 初始化 异常信息 收集
     */
    private void initCrash() {
        CrashHandler.instance(mContext);
    }

    /**
     * 初始化日志管理
     */
    private void initLogger() {
//        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
//                .tag(" - Inz - ")
//                .build();
//        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
//            @Override
//            public boolean isLoggable(int priority, @Nullable String tag) {
//                return BuildConfig.DEBUG;
//            }
//        });
//        Logger.i("初始化 Logger Logcat 完成 : " + Tools.baseDateFormat.format(System.currentTimeMillis()));
        // 保存到本地
        FormatStrategy fileStrategy = MyCsvFormatStrategy.newBuilder()
                .dateFormat((SimpleDateFormat) Tools.getBaseDateFormat())
                .tag(" - Inz - ")
                .build();
        Logger.addLogAdapter(new DiskLogAdapter(fileStrategy));
        Logger.i("初始化 Logger File 完成 : " + Tools.getBaseDateFormat().format(System.currentTimeMillis()));
    }

    /**
     * 监听整个程序
     */
    private class InzActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Logger.i("onActivityCreated - " + activity.getLocalClassName() + ";");
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Logger.i("onActivityStarted - " + activity.getLocalClassName() + ";");
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Logger.i("onActivityResumed - " + activity.getLocalClassName() + ";");
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Logger.i("onActivityCreated - " + activity.getLocalClassName() + ";");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Logger.i("onActivityStopped - " + activity.getLocalClassName() + ";");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Logger.i("onActivitySaveInstanceState - " + activity.getLocalClassName() + ";");
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Logger.i("onActivityDestroyed - " + activity.getLocalClassName() + ";");
        }
    }
}
