package com.inz.z.view.fragment

import androidx.cardview.widget.CardView
import com.inz.z.R

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/9 16:07.
 */
class TiwCardViewFragment : AbsBaseFragment() {

    private var mCardView: androidx.cardview.widget.CardView? = null

    override fun getLayoutId(): Int {
        return R.layout.tiw_item_card
    }

    override fun initView() {
        mCardView = mView.findViewById(R.id.tiw_item_card_cv)
    }

    override fun initData() {
    }
}