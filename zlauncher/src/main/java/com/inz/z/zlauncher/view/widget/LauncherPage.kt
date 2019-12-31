package com.inz.z.zlauncher.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

/**
 * 运行页
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/12/31 14:39.
 */
class LauncherPage : ViewGroup {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }
}