package com.inz.z.view.adapter

import android.content.Context
import android.support.v4.view.ViewPager
import android.support.v7.widget.CardView
import android.util.Log
import android.view.View
import org.jetbrains.annotations.NotNull

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/9 15:04.
 */
class TiwCardPagerTransformer : ViewPager.PageTransformer {
    private var mContext: Context? = null
    /**
     * ViewPager
     */
    private var mViewPager: ViewPager? = null

    /**
     * X轴 偏移量
     */
    private var maxTransformerOffsetX: Int = 0

    constructor(mContext: Context?) {
        this.mContext = mContext
        this.maxTransformerOffsetX = dp2px(mContext!!, 120F)
    }

    constructor(mContext: Context?, @NotNull maxTransformerOffsetX: Int) {
        this.mContext = mContext
        this.maxTransformerOffsetX = dp2px(mContext!!, maxTransformerOffsetX.toFloat())
    }


    override fun transformPage(p0: View, p1: Float) {
        if (mViewPager == null) {
            mViewPager = p0.parent as ViewPager?
        }
        Log.i("transformPage", "view = " + p0 + " ; postion = " + p1)
        val view: CardView = p0 as CardView
        if (mViewPager == null) {
            return
        }

        val scale = if (p1 < 0) {
            (1 - 0.8) * p1 + 1
        } else {
            (0.8 - 1) * p1 + 1
        }
        p0.translationX = Math.abs(p1)
//        p0.pivotY = (p0.height / 2).toFloat()
        val sW = p0.measuredWidth
        val cW = view.measuredWidth
        p0.scaleX = scale.toFloat()
        p0.scaleY = scale.toFloat()

    }


    /**
     * 将 dp 转换成 px
     *
     * @return px(像素)
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

}