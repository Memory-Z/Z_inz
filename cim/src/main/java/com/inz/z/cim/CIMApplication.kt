package com.inz.z.cim

import android.app.Application
import android.util.Log
import com.farsunset.cim.sdk.android.CIMPushManager

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/09/29 15:57.
 */
class CIMApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CIMPushManager.setLoggerEnable(applicationContext, true)
        CIMPushManager.connect(applicationContext, "192.168.1.136", 23456)
        val connedted = CIMPushManager.isConnected(applicationContext)
        Log.i("MAINIXIA", "-----------------------> $connedted")
    }
}