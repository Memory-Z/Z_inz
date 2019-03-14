package com.inz.z.app_update.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/13 11:32.
 */
class NetUtils {
    companion object {
        /**
         * 获取网路状态
         */
        fun getNetworkStatus(context: Context): Boolean {
            val manager: ConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = manager.activeNetworkInfo
            if (networkInfo != null) {
                return networkInfo.isConnected
            }
            return false
        }
    }
}