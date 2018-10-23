package com.inz.z_inz;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.inz.z_inz.util.FileUtils;
import com.inz.z_inz.util.SPHelper;

import java.io.File;

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
        mContext = this;
        // 注册 Activity 生命周期 回调 函数
        registerActivityLifecycleCallbacks(new InzActivityLifecycleCallbacks());
        // 初始化 SharePreferences
        SPHelper.getInstance().initSharedPreferences(mContext);
        // 创建 项目的文件目录
        createFile();
    }

    /**
     * 创建文件
     */
    private void createFile() {
        File file = new File(FileUtils.getSDPath() + "/com.inz.z_inz/");
        boolean flag = true;
        if (!file.exists()) {
            flag = file.mkdir();
        }
        Log.i(TAG, "createFile: 文件夹 - 创建" + flag + " ： " + file.getAbsolutePath());
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
