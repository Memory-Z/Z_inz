package com.inz.z.view.fragment.example

import com.inz.z.R
import com.inz.z.view.fragment.AbsBaseFragment

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/3 10:48.
 */
class CircleProgressFragment : AbsBaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.ex_fragment_circle_progress
    }

    override fun initView() {
//        val circleProgressView: CircleProgressView =
//            mView.findViewById(R.id.ex_fragment_circle_progress_cpv)
//        circleProgressView.currentProgress = 10F
//        if (circleProgressView.currentProgress < circleProgressView.allProgress) {
//            circleProgressView.currentProgress += 10F
//            circleProgressView.postInvalidateDelayed(1000)
//        }
    }

    override fun initData() {
    }
}