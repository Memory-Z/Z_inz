package com.inz.z.note_book.view.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.CheckedTextView
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.widget.TintTypedArray
import com.inz.z.base.view.widget.BaseRichEditText
import com.inz.z.note_book.R
import kotlinx.android.synthetic.main.widget_schedule_item_layout.view.*

/**
 * 计划TextView
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/05 10:58.
 */
class ScheduleLayout : LinearLayout {

    companion object {
        const val TAG = "ScheduleLayout"
    }

    /**
     * 内容
     */
    private var contentStr = ""
    /**
     * 更新时间
     */
    private var updateTimeStr = ""
    /**
     * 内容文字大小
     */
    private var contentTextSize = 24F
    /**
     * 是否显示选择框-
     */
    private var haveScheduleCheckBox = false

    private var mView: View? = null
    private var mCheckBox: CheckBox? = null
    private var mEditText: EditText? = null


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
        initAttr(context, attrs)
        initParameter()
    }

    /**
     * 初始化 视图
     */
    private fun initView(context: Context?) {
        if (mView == null) {
            mView = LayoutInflater.from(context)
                .inflate(R.layout.widget_schedule_item_layout, this, true)
            mCheckBox = mView!!.widget_schedule_check_box
            mEditText = mView!!.widget_schedule_et
        }
    }

    /**
     * 初始化配置信息
     */
    private fun initAttr(context: Context?, attrs: AttributeSet?) {
        val array =
            TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.ScheduleLayout, 0, 0)
        haveScheduleCheckBox =
            array.getBoolean(R.styleable.ScheduleLayout_have_schedule_check_box, false)
        array.recycle()
    }

    /**
     * 初始化 参数
     */
    private fun initParameter() {
        mCheckBox?.visibility = if (haveScheduleCheckBox) View.VISIBLE else View.GONE
        setContent(contentStr)
    }

    fun setContent(content: String) {
        contentStr = content
        mEditText?.apply {
            setText(content)
            setSelection(content.length)
        }

    }

    fun getContent(): String {
        return mEditText?.text.toString()
    }


    /* ---------------------------- 设置 高度 ------------------------------ */
    fun setEditTextHeight(height: Float) {
        val lp = mEditText?.layoutParams
        lp?.height = height.toInt()
        mEditText?.layoutParams = lp
    }

}