package com.inz.z.app_update.utils

import android.content.Context
import android.content.pm.PackageManager

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/9 15:06.
 */
class PackageUtils {

    public fun getVersionCode(context: Context): Int {
        val manager = context.packageManager
        try {
            val info = manager.getPackageInfo(context.packageName, 0) ?: return 0
            return info.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            return 0
        }

    }

    public fun getVersionName(context: Context): String {
        val manager = context.packageManager
        try {
            val info = manager.getPackageInfo(context.packageName, 0) ?: return "1.0"
            return info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            return "1.0"
        }

    }
}