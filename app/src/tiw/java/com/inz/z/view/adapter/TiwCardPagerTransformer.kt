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
    private val minAlpha = .4F
    private val minScale = .6F
    private val maxScale = .8F

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

    constructor(mContext: Context?, mViewPager: ViewPager?) {
        this.mContext = mContext
        this.mViewPager = mViewPager
        this.maxTransformerOffsetX = dp2px(mContext!!, 120F)
    }


    override fun transformPage(page: View, position: Float) {
        if (mViewPager == null) {
            mViewPager = page.parent as ViewPager?
        }
        if (mViewPager == null) {
            return
        }
        // 父元素 宽
        val pWidth = mViewPager!!.measuredWidth
        // 每页 宽
        val tW = page.measuredWidth
        // 距两边距离
        val dW = (pWidth - tW) / 2
//        val pScaleX = page.scaleX
//        if (position <= 0) {
//            page.alpha = 0F
//        } else if (position <= 1) {
        page.alpha = Math.max(minAlpha, 1 - Math.abs(position * minAlpha))
        if (position <= 1) {
            val scale = Math.min(maxScale, Math.max(Math.abs(1 - position * minScale), minScale))
//            val scale = minScale + (1 - minScale) * (1 - position)
            page.scaleX = scale
            page.scaleY = scale
        } else {
            val scale = Math.max(minScale, Math.abs(Math.abs(position) - 1) * minScale)
//            val w = dW * position
////            val scale = minScale + (1 - minScale) * (1 - position)
//            page.translationX = w
            page.scaleX = scale
            page.scaleY = scale
        }
//        val scale = Math.max(minScale, Math.abs(1 - Math.abs(position)))
//        page.scaleX = scale
//        page.scaleY = scale
//        if (position < -1) {
//            val scale = Math.max(minScale, Math.abs(1 - Math.abs(position)))
//            page.scaleX = scale
//            page.scaleY = scale
//        } else if (position <= 0) {
//            val scale = Math.min(minScale, 1 - Math.abs(position))
//            page.scaleX = scale
//            page.scaleY = scale
//        } else
//            if (position <= 1) {
//            val scale = Math.max(minScale, Math.abs(position))
//            page.scaleX = scale
//            page.scaleY = scale
//        } else {
//            val scale = Math.min(minScale, Math.abs(1 - Math.abs(position)))
//            page.scaleX = scale
//            page.scaleY = scale
//        }
        Log.i("transformPage", "view = $page ; position = $position")
    }


    /**
     * 将 dp 转换成 px
     *
     * @return px(像素)
     */
    private fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

}