package com.inz.z.zlauncher

import android.app.Application
import com.inz.z.base.util.L

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/12/31 10:20.
 */
class MyApplication : Application() {
    companion object {
        const val TAG = "MyApplication"
    }

    override fun onCreate() {
        super.onCreate()
        L.initL(applicationContext)

    }
}