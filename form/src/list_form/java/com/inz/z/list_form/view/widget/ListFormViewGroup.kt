package com.inz.z.list_form.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.ListView

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2020/01/08 11:22.
 */
class ListFormViewGroup : LinearLayout {

    private var mContext: Context? = null
    private var mView: View? = null

    companion object {
        private const val TAG = "ListFormViewGroup"
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private fun initView() {
        if (mView == null) {

        }
    }


}