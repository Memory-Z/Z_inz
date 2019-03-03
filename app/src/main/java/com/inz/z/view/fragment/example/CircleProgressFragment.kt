package com.inz.z.view.fragment.example

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inz.z.R
import com.inz.z.view.fragment.AbsBaseFragment
import com.inz.z.view.widget.CircleProgressView
import kotlin.concurrent.fixedRateTimer

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/3 10:48.
 */
class CircleProgressFragment : AbsBaseFragment() {

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ex_fragment_circle_progress, null, false)
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