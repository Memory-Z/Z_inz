package com.inz.z.view.adapter

import android.content.Context
import android.graphics.Color
import androidx.viewpager.widget.PagerAdapter
import android.view.View
import android.view.ViewGroup

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/9 15:50.
 */
class TiwCardAdapter : androidx.viewpager.widget.PagerAdapter {
    private var mContext: Context? = null
    private var viewList: MutableList<View>? = null

    constructor(mContext: Context?) : super() {
        this.mContext = mContext
        this.viewList = ArrayList()
    }

    constructor(mContext: Context?, viewList: MutableList<View>?) : super() {
        this.mContext = mContext
        this.viewList = viewList
    }


    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    override fun getCount(): Int {
        return if (viewList != null) viewList!!.size else 0
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val mView: View? = viewList?.get(position)
        if (mView == null) {
            return super.instantiateItem(container, position)
        } else {
            mView.setBackgroundColor(Color.BLUE)
            container.addView(mView)
            return mView
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(viewList?.get(position))
    }
}