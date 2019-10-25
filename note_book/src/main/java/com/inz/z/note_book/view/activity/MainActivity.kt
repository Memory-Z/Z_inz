package com.inz.z.note_book.view.activity

import android.view.View
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.inz.z.base.view.AbsBaseActivity
import com.inz.z.note_book.R

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/17 14:40.
 */
open class MainActivity : AbsBaseActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun initWindow() {

    }

    override fun getLayoutId(): Int {
        return R.layout.main_layout
    }

    override fun initView() {
    }

    override fun initData() {
    }
}