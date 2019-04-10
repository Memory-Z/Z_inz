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


    override fun transformPage(page: View, position: Float) {
        if (mViewPager == null) {
            mViewPager = page.parent as ViewPager?
        }
        if (mViewPager == null) {
            return
        }
        // 父元素 宽
        val pWidth = mViewPager!!.measuredWidth
        val tW = page.measuredWidth
        val dW = (pWidth - tW) / 2
        val pScaleX = page.scaleX
//        if (position <= 0) {
//            page.alpha = 0F
//        } else if (position <= 1) {
        page.alpha = 1 - Math.abs(position) + Math.abs(dW / tW)
//        page.translationX = -dW.toFloat()
//        } else if (position > 1) {
//            page.alpha = 1F
//        }
        Log.i("transformPage", "view = " + page + " ; postion = " + position)

        val scale = if (position < 0) {
            (1 - 0.8) * position + 1
        } else {
            (0.8 - 1) * position + 1
        }
//        page.translationX = Math.abs(position)
//        p0.pivotY = (p0.height / 2).toFloat()
//        val sW = page.measuredWidth
//        page.scaleX = position
//        page.scaleY = position

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