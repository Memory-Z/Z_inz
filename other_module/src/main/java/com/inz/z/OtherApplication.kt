package com.inz.z

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/12 10:25.
 */
public open class OtherApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    override fun onCreate() {
        super.onCreate()

    }
}