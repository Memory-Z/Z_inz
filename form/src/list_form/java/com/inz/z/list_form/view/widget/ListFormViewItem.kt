package com.inz.z.list_form.view.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.inz.z.form.R
import java.awt.font.TextAttribute

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2020/01/08 11:38.
 */
class ListFormViewItem : View {
    private var mContext: Context? = null
    private var mView: View? = null
    private var bean: ListFormBean? = null

    companion object {
        private const val TAG = "ListFormViewItem"
    }

    private lateinit var contentPaint: Paint
    private var contentTextSize = 24.0F
    private var contentMsg = ""

    private lateinit var leftIconPaint: Paint
    private var leftIconId: Int = 0x0

    private lateinit var rightPaint: Paint
    private var rightIconId: Int = 0x0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        initPaint()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    private fun initPaint() {
        R.color.tv_color
        contentPaint = Paint()
            .apply {
                textSize = contentTextSize
                isAntiAlias = true
                color = Color.parseColor("#FF474747")
            }
        leftIconPaint = Paint()
            .apply {
                isAntiAlias = true
                style = Paint.Style.STROKE
            }
        rightPaint = Paint()
            .apply {
                isAntiAlias = true
                style = Paint.Style.STROKE
                strokeWidth = 10F
            }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var centerX = width.div(2)
        var centerY = height.div(2)
        if (bean != null) {
            val content = bean!!.content
        }
    }
}