package com.inz.z_inz.activity;

import android.app.Application;
import android.content.Context;

import com.inz.z_inz.util.FileUtils;
import com.inz.z_inz.util.SPHelper;

import java.io.File;

/**
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * @date Create By 2018/7/21 10:13
 */
public class MyApplication extends Application {

    public static boolean escFlag = false;
    // 上下文
    private Context mContext;
//    private


    public MyApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
        // 初始化 SharePreferences
        SPHelper.getInstance().initSharedPreferences(mContext);

        createFile();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 创建文件
     */
    private void createFile() {
        File file = new File(FileUtils.getSDPath() + "/com.inz.z_inz/");
        if (!file.exists()) {
            file.mkdir();
        }
    }

}
