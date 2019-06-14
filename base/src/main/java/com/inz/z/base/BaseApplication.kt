package com.inz.z.base

import android.app.Application
import android.content.Context
import com.inz.z.base.util.CrashHandler

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/15 9:17.
 */
open class BaseApplication : Application() {
    var mContext: Context? = null

    override fun onCreate() {
        super.onCreate()
        if (mContext == null) {
            mContext = this;
        }
        // 崩溃信息处理 初始化
        CrashHandler.instance(mContext)
    }
}