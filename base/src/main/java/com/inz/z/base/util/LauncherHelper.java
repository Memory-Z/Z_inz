package com.inz.z.base.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 运行帮助类
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/25 15:42.
 */
public class LauncherHelper {

    /**
     * 启动其他软件
     *
     * @param context     上下文
     * @param packageName 包名
     */
    public static void launcherPackageName(Context context, String packageName) {
        if (context == null) {
            throw new RuntimeException(" context is null . can`t run other application. ");
        }
        PackageManager packageManager = context.getPackageManager();
        if (checkPackageName(packageManager, packageName)) {
            Intent intent = packageManager.getLaunchIntentForPackage(packageName);
            context.startActivity(intent);
        } else {
            throw new RuntimeException(" not find this package: " + packageName + " . ");
        }

    }

    /**
     * 检测包名是否存在
     *
     * @param manager     管理器
     * @param packageName 包名
     * @return 是否存在
     */
    private static boolean checkPackageName(PackageManager manager, String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = manager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }


}
