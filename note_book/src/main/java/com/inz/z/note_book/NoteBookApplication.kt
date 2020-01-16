package com.inz.z.note_book

import android.app.Application
import com.inz.z.base.util.CrashHandler
import com.inz.z.base.util.L
import com.inz.z.note_book.database.util.GreenDaoHelper
import com.inz.z.note_book.util.SPHelper

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/17 14:39.
 */
class NoteBookApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        L.initL(applicationContext)
        CrashHandler.instance(applicationContext)
        GreenDaoHelper.getInstance().initGreenDaoHelper(applicationContext)
        SPHelper.init(applicationContext)
    }
}