package com.inz.z;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.Keep;
import android.util.Log;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/18 10:12.
 */
public class SophixStubApplication extends SophixApplication {

    private static final String TAG = "SophixStubApplication";

    // 此处SophixEntry应指定真正的Application，并且保证RealApplicationStub类名不被混淆。
    @Keep
    @SophixEntry(InzApplication.class)
    static class RealApplicationStub {

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 如果需要使用MultiDex，需要在此处调用。
        // MultiDex.install(this);
        initSophix();
    }

    private void initSophix() {

        String appVersion = "0.0.0";
        try {
            appVersion = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0)
                    .versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        SophixManager instance = SophixManager.getInstance();
        instance.setContext(this)
                .setAppVersion(appVersion)
                .setSecretMetaData(null, null, null)
                .setEnableDebug(true)
                .setEnableFullLog()
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(int mode, int code, String info, int handlePatchVersion) {
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            Log.i(TAG, "onLoad: sophix load path success!");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 如果需要在后台重启，建议此处用SharePreference保存状态。
                            Log.i(TAG, "sophix preload patch success. restart app to make effect.");
                        }
                    }
                })
                .initialize();
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
