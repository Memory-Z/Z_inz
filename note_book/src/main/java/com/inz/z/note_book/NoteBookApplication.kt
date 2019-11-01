package com.inz.z.note_book

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import com.inz.z.base.util.L
import com.inz.z.note_book.database.DaoMaster
import com.inz.z.note_book.database.DaoSession
import com.inz.z.note_book.database.util.GreenDaoHelper

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
        GreenDaoHelper.getInstance().initGreenDaoHelper(applicationContext)
    }
}