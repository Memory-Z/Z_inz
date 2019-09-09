package com.inz.z.view.adapter

import android.content.Context
import androidx.viewpager.widget.ViewPager
import androidx.cardview.widget.CardView
import android.util.Log
import android.view.View
import org.jetbrains.annotations.NotNull

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/9 15:04.
 */
class TiwCardPagerTransformer : androidx.viewpager.widget.ViewPager.PageTransformer {
    private var mContext: Context? = null
    /**
     * ViewPager
     */
    private var mViewPager: androidx.viewpager.widget.ViewPager? = null
    private val minAlpha = .4F
    private val minScale = .6F
    private val maxScale = .8F

    private val defaultCenter = .5F

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

    constructor(mContext: Context?, mViewPager: androidx.viewpager.widget.ViewPager?) {
        this.mContext = mContext
        this.mViewPager = mViewPager
        this.maxTransformerOffsetX = dp2px(mContext!!, 120F)
    }


    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val pageHeight = page.height

        if (mViewPager == null) {
            mViewPager = page.parent as androidx.viewpager.widget.ViewPager?
        }

        page.pivotY = (pageHeight / 2).toFloat()

        if (position < -1) {  // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.scaleX = minScale
            page.scaleY = minScale
            page.pivotX = pageWidth.toFloat()
        } else if (position <= 1) {  // [-1,1]
            // Modify the default slide transition to shrink the page as well
            //1->2: 1[0,-1] / 2[1,0]
            //2->1: 1[-1,0] / 2[0,1]
            val pw = mViewPager!!.width
            val pageSw: Float = (pageWidth / pw).toFloat()
            val pageSnw: Float = (((pw - pageWidth) / 2) / pw).toFloat()
            var scaleFactor = 1F
            if (position < 0) {
                scaleFactor = (1 + position) * (1 - minScale) + minScale
            } else {
//                if (pw * position < (pw - pageWidth) / 2) {
//                    scaleFactor = (position) * (1 - minScale) + minScale
//                } else {
                    scaleFactor = (1 - position) * (1 - minScale) + minScale
//                }
            }
            page.scaleX = scaleFactor
            page.scaleY = scaleFactor
            page.pivotX = pageWidth * ((1 - position) * defaultCenter)
        } else { //(1,Infinity]
            page.pivotX = 0F
            page.scaleX = minScale
            page.scaleY = minScale
        }
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