package com.inz.z.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.TintTypedArray
import android.util.AttributeSet
import android.view.View
import com.inz.z.R

/**
 *
 * @author 11654
 * @version 1.0.0
 * Create By Zhenglj on 2019/3/25 22:47
 */
class TiwActionLayout : ConstraintLayout {
    private var mContext: Context? = null
    /**
     * 左侧布局
     */
    private var mLeftView: View? = null
    /**
     * 右侧布局
     */
    private var mRightView: View? = null
    /**
     * 中间布局
     */
    private var mCenterView: View? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mContext = context

    }

    @SuppressLint("RestrictedApi")
    private fun initStyleAttrs(attrs: AttributeSet) {
        val tintTypedArray = TintTypedArray.obtainStyledAttributes(mContext!!, attrs,
                R.styleable.TiwActionLayout, 0, 0)
        val leftIcon = tintTypedArray.getResourceId(R.styleable.TiwActionLayout_tiw_action_left_icon, R.layout.tiw_action_layout)
        tintTypedArray.recycle()
    }
}