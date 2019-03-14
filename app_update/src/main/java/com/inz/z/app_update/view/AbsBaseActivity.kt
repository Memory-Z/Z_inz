package com.inz.z.app_update.view

import android.content.Context
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/13 15:07.
 */
abstract class AbsBaseActivity : AppCompatActivity() {

    protected var mContext: Context? = null

    protected abstract fun getUpdateDialogFragment(): Fragment

    protected abstract fun getDownloadDialogFragment(): Fragment

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected fun initWindow() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindow()
        setContentView(getLayoutId())
        mContext = this
    }
}