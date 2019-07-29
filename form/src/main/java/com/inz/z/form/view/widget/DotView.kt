package com.inz.z.form.view.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/26 09:31.
 */
class DotView : View {
    companion object {
        private const val TAG = "DotView"
    }

    private var mContext: Context? = null
    private var mDotPaint: Paint? = null
    private var mLabelPaint: Paint? = null
    private var mLabelTextPaint: Paint? = null
    private var mLabelText = "标签"
    private var mLabelColor: Int = Color.WHITE
    private var isShowLabel = false
    private var mLabelPath: Path? = null
    private var mLabelTextRect: Rect? = null


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mContext = context
        initParameter()
    }

    /**
     * 初始化参数
     */
    private fun initParameter() {
        isClickable = true
        isFocusable = true
        mDotPaint = Paint()
        mDotPaint!!.isAntiAlias = true
        mDotPaint!!.style = Paint.Style.FILL
        mDotPaint!!.color = Color.WHITE

        mLabelPaint = Paint()
        mLabelPaint!!.isAntiAlias = true
        mLabelPaint!!.style = Paint.Style.STROKE
        mLabelPaint!!.color = Color.WHITE
        mLabelPaint!!.strokeWidth = 4F
        mLabelPaint!!.strokeJoin = Paint.Join.ROUND

        mLabelTextPaint = Paint()
        mLabelTextPaint!!.isAntiAlias = true
        mLabelTextPaint!!.style = Paint.Style.STROKE
        mLabelTextPaint!!.color = Color.WHITE
        mLabelTextPaint!!.strokeWidth = 4F
        mLabelTextPaint!!.textSize = 32F
        mLabelTextPaint!!.textAlign = Paint.Align.LEFT

        mLabelPath = Path();
        mLabelTextRect = Rect()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val centerX: Float = (width / 2).toFloat()
        val centerY: Float = (height / 2).toFloat()

        canvas?.drawCircle(centerX, centerY, 60F, mDotPaint!!)

        if (isShowLabel) {
            val textLf = centerX + 60F
            if (mLabelText.length > 10) {
                mLabelText = mLabelText.substring(0, 10) + "..."
            }
            val fontMetrics = mLabelTextPaint!!.fontMetrics
            val centerTextY =
                centerY + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom

            mLabelTextPaint!!.getTextBounds(mLabelText, 0, mLabelText.length, mLabelTextRect!!)
            val textWidth = mLabelTextRect!!.width()
            val textHeight = mLabelTextRect!!.height()


            mLabelPath!!.moveTo(centerX + 30F, centerY)
            mLabelPath!!.lineTo(textLf, centerY - textHeight / 2 - 30F)
            mLabelPath!!.lineTo(textLf + textWidth + 30F, centerY - textHeight / 2 - 30F)
            mLabelPath!!.lineTo(textLf + textWidth + 30F, centerY + textHeight / 2 + 30F)
            mLabelPath!!.lineTo(textLf, centerY + textHeight / 2 + 30F)
            mLabelPath!!.lineTo(centerX + 30F, centerY)

            canvas?.drawPath(mLabelPath!!, mLabelPaint!!)

            canvas?.drawText(mLabelText, textLf, centerTextY, mLabelTextPaint!!)
        }
    }

    override fun performContextClick(x: Float, y: Float): Boolean {
        isShowLabel = true
        invalidate()
        postDelayed({
            isShowLabel = false
            invalidate()
        }, 10 * 1000)
        return super.performContextClick(x, y)
    }

    override fun performContextClick(): Boolean {
        isShowLabel = true
        invalidate()
        postDelayed({
            isShowLabel = false
            invalidate()
        }, 10 * 1000)
        return super.performContextClick()
    }
}