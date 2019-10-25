package com.inz.z.note_book

import android.app.Application
import com.inz.z.base.util.L

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
    }
}