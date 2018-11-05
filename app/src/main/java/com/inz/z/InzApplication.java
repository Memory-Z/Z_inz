package com.inz.z;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.inz.z.entity.Constants;
import com.inz.z.util.CrashHandler;
import com.inz.z.util.FileUtils;
import com.inz.z.util.SPHelper;
import com.inz.z.util.Tools;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;

import java.io.File;
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
        // 非 测试环境
        if (!Constants.isIsTest()) {
            // 初始化 异常信息收集
            initCrash();
        }
        // 注册 Activity 生命周期 回调 函数
        registerActivityLifecycleCallbacks(new InzActivityLifecycleCallbacks());
        // 初始化 SharePreferences
        SPHelper.getInstance().initSharedPreferences(mContext);
        // 创建 项目的文件目录
        createFile();
        Log.i(TAG, "onCreate: " + System.currentTimeMillis());
        // 初始化 日志
        initLogger();
    }

    /**
     * 初始化 异常信息 收集
     */
    private void initCrash() {
        CrashHandler.instance(mContext);
    }

    /**
     * 创建文件
     */
    private void createFile() {
        File file = new File(FileUtils.getSDPath() + Constants.getBaseDirPath());
        boolean flag = true;
        if (!file.exists()) {
            flag = file.mkdirs();
        }
        Log.i(TAG, "createFile: 文件夹 - 创建" + flag + " ： " + file.getAbsolutePath());
    }

    /**
     * 初始化日志管理
     */
    private void initLogger() {
//        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
//                .tag("SKYVIS_LinkCourt")
//                .build();
//        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
//            @Override
//            public boolean isLoggable(int priority, @Nullable String tag) {
//                return BuildConfig.DEBUG;
//            }
//        });
//        Logger.i("初始化 Logcat 完成");
        // 保存到本地
        FormatStrategy fileStrategy = CsvFormatStrategy.newBuilder()
                .dateFormat((SimpleDateFormat) Tools.baseDateFormat)
                .tag("SKYVIS_LinkCourt")
                .build();
        Logger.addLogAdapter(new DiskLogAdapter(fileStrategy));
        Logger.i("初始化 Logger File 完成");
    }

    /**
     * 监听整个程序
     */
    private class InzActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}
