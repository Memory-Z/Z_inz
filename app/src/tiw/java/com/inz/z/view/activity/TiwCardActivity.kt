package com.inz.z.view.activity

import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.cardview.widget.CardView
import android.view.KeyEvent
import android.view.View
import com.inz.z.R
import com.inz.z.view.adapter.TiwCardAdapter
import com.inz.z.view.adapter.TiwCardPagerTransformer
import kotlinx.android.synthetic.tiw.tiw_card_layout.*

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/9 14:54.
 */
class TiwCardActivity : AbsBaseActivity() {

    private var mViewPager: androidx.viewpager.widget.ViewPager? = null
    private var mTabLayout: TabLayout? = null

    override fun initWindow() {
    }

    override fun getContentViewId(): Int {
        return R.layout.tiw_card_layout
    }

    override fun initView() {
        mViewPager = tiw_card_vp
        mTabLayout = tiw_card_tl

    }

    override fun initData() {
        val viewList: MutableList<View> = ArrayList()
        val card0 = androidx.cardview.widget.CardView(mContext)
        viewList.add(card0)
        val card1 = androidx.cardview.widget.CardView(mContext)
        viewList.add(card1)
        val card2 = androidx.cardview.widget.CardView(mContext)
        viewList.add(card2)
        val card3 = androidx.cardview.widget.CardView(mContext)
        viewList.add(card3)
        val card4 = androidx.cardview.widget.CardView(mContext)
        viewList.add(card4)
        val card5 = androidx.cardview.widget.CardView(mContext)
        viewList.add(card5)
        mViewPager?.adapter = TiwCardAdapter(mContext, viewList)
        mViewPager?.setPageTransformer(false, TiwCardPagerTransformer(mContext))
//        mViewPager?.offscreenPageLimit = 3
//        mViewPager?.pageMargin = -10
    }

    override fun myOnKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }
}