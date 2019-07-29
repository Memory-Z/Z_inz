package com.inz.z.form.view.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.min

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/24 17:05.
 */
class BaseLineChartView : View, View.OnTouchListener {
    companion object {
        private const val TAG = "BaseLineChartView"
    }

    private var mContext: Context? = null

    /**
     * 内线
     */
    private var mInnerLinePaint: Paint? = null
    /**
     * 外线
     */
    private var mOuterLinePaint: Paint? = null
    /**
     * 外数字
     */
    private var mOuterLineNumPaint: Paint? = null

    /**
     * 需要绘制的线
     */
    private var mLinePaint: Paint? = null
    private var arrowPath: Path = Path()

    private val innerList: ArrayList<Float> = ArrayList()

    private val maxNum: Float = 100F
    private val minNum: Float = 0F
    val xUnit: String = "年份"
    val yUnit: String = "金额"

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mContext = context
        initFormParameter()
        initDotParameter()
        setOnTouchListener(this)
//        addDot()
    }

    /**
     * 初始化表单属性
     */
    private fun initFormParameter() {

        mInnerLinePaint = Paint()
        mInnerLinePaint!!.isAntiAlias = true
        mInnerLinePaint!!.style = Paint.Style.STROKE
        mInnerLinePaint!!.color = Color.GRAY
        mInnerLinePaint!!.strokeWidth = 1F
        mInnerLinePaint!!.pathEffect = DashPathEffect(floatArrayOf(6F, 4F), 2F)

        mOuterLinePaint = Paint()
        mOuterLinePaint!!.isAntiAlias = true
        mOuterLinePaint!!.style = Paint.Style.STROKE
        mOuterLinePaint!!.color = Color.BLACK
        mOuterLinePaint!!.strokeWidth = 4F

        mOuterLineNumPaint = Paint()
        mOuterLineNumPaint!!.isAntiAlias = true
        mOuterLineNumPaint!!.color = Color.BLACK
        mOuterLineNumPaint!!.textAlign = Paint.Align.CENTER
        mOuterLineNumPaint!!.textSize = 36F

        mLinePaint = Paint()
        mLinePaint!!.isAntiAlias = true
        mLinePaint!!.style = Paint.Style.STROKE
        mLinePaint!!.color = Color.BLACK
        mLinePaint!!.strokeWidth = 2F

    }

    private var mDotPaint: Paint? = null
    private var mLabelPaint: Paint? = null
    private var mLabelTextPaint: Paint? = null
    private var mLabelText = "标签"
    private var mLabelColor: Int = Color.WHITE
    private var isShowLabel = false
    private var mLabelPath: Path? = null
    private var mLabelTextRect: Rect? = null

    /**
     * 初始化参数
     */
    private fun initDotParameter() {
        isClickable = true
        isFocusable = true
        mDotPaint = Paint()
        mDotPaint!!.isAntiAlias = true
        mDotPaint!!.style = Paint.Style.FILL
        mDotPaint!!.color = Color.BLACK

        mLabelPaint = Paint()
        mLabelPaint!!.isAntiAlias = true
        mLabelPaint!!.style = Paint.Style.FILL
        mLabelPaint!!.color = Color.GRAY
        mLabelPaint!!.strokeWidth = 4F
        mLabelPaint!!.strokeJoin = Paint.Join.ROUND

        mLabelTextPaint = Paint()
        mLabelTextPaint!!.isAntiAlias = true
        mLabelTextPaint!!.color = Color.BLACK
        mLabelTextPaint!!.strokeWidth = 2F
        mLabelTextPaint!!.textSize = 32F
        mLabelTextPaint!!.textAlign = Paint.Align.LEFT

        mLabelPath = Path();
        mLabelTextRect = Rect()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 垂直间距
        val difH: Float = (height * .05).toFloat()
        // 水平间距
        val difW: Float = (width * .05).toFloat()
        // 垂直边距
        val marH: Float = difH * 2

        // 确定原点坐标
        val oX = difW * 3
        val oY = height - marH
        // 最小间距
        val minMar = min(difH, difW)

        drawBackgroundForm(canvas, oX, oY, minMar)
        drawDot(canvas)
    }

    /**
     * 绘制背景表盘
     */
    private fun drawBackgroundForm(canvas: Canvas?, oX: Float, oY: Float, minMar: Float) {
        // 横线数
        val wNum: Int = (height * .75 / minMar).toInt()
        val hNum: Int = (width * .75 / minMar).toInt()
        // X，Y 轴长度
        val xLength = minMar * hNum
        val yLength = minMar * wNum

        mOuterLineNumPaint!!.textAlign = Paint.Align.RIGHT
        // 横线
        innerList.clear()
        for (position in 0..wNum) {
            innerList.add(oX)
            innerList.add(oY - minMar * position)
            innerList.add(oX + xLength)
            innerList.add(oY - minMar * position)
            if (position < wNum - 1) {
                canvas?.drawText(
                    (100000 + position * 10000).toString(),
                    oX - minMar / 2,
                    oY - minMar * (position + 1) + minMar / 4,
                    mOuterLineNumPaint!!
                )
            }
        }

        mOuterLineNumPaint!!.textAlign = Paint.Align.CENTER

        // 竖线
        for (position in 0..hNum) {
            innerList.add(oX + minMar * position)
            innerList.add(oY)
            innerList.add(oX + minMar * position)
            innerList.add(oY - yLength)

            if (position % 2 == 0 && position < hNum - 1) {
                canvas?.save()
                canvas?.rotate(-45F, oX + minMar * (position + 1), oY + minMar)
                canvas?.drawText(
                    (2012 + position).toString(),
                    oX + minMar * (position + 1),
                    oY + minMar,
                    mOuterLineNumPaint!!
                )
                canvas?.restore()
            }
        }
        canvas?.drawLines(innerList.toFloatArray(), mInnerLinePaint!!)

        canvas?.drawLine(oX, oY, oX, oY - yLength - minMar, mOuterLinePaint!!)
        canvas?.drawLine(oX, oY, oX + xLength + minMar, oY, mOuterLinePaint!!)

        arrowPath.moveTo(oX, oY - yLength - minMar)
        arrowPath.lineTo(oX - 2, oY - yLength - minMar + 6)
        arrowPath.lineTo(oX + 2, oY - yLength - minMar + 6)
        arrowPath.close()
        canvas?.drawPath(arrowPath, mOuterLinePaint!!)

        arrowPath.moveTo(oX + xLength + minMar, oY)
        arrowPath.lineTo(oX + xLength + minMar - 6, oY + 2)
        arrowPath.lineTo(oX + xLength + minMar - 6, oY - 2)
        arrowPath.close()
        canvas?.drawPath(arrowPath, mOuterLinePaint!!)

        canvas?.drawText("0", oX - minMar / 2, oY + minMar, mOuterLineNumPaint!!)
        canvas?.drawText(
            "Y / $yUnit",
            oX - minMar / 2,
            oY - yLength - minMar - minMar / 2,
            mOuterLineNumPaint!!
        )
        canvas?.drawText(
            "X / $xUnit",
            oX + xLength + minMar / 2,
            oY + minMar,
            mOuterLineNumPaint!!
        )
    }

    private fun drawDot(canvas: Canvas?) {
        val centerX: Float = (width / 2).toFloat()
        val centerY: Float = (height / 2).toFloat()

        canvas?.drawCircle(centerX, centerY, 20F, mDotPaint!!)

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
            mLabelPath!!.lineTo(textLf, centerY - textHeight / 2 - 16F)
            mLabelPath!!.lineTo(textLf + textWidth + 30F, centerY - textHeight / 2 - 16F)
            mLabelPath!!.lineTo(textLf + textWidth + 30F, centerY + textHeight / 2 + 16F)
            mLabelPath!!.lineTo(textLf, centerY + textHeight / 2 + 16F)
            mLabelPath!!.lineTo(centerX + 30F, centerY)

            canvas?.drawPath(mLabelPath!!, mLabelPaint!!)

            canvas?.drawText(mLabelText, textLf + 8F, centerTextY, mLabelTextPaint!!)
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val x = event?.x!!
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                Log.i(TAG, "-------------------------- x = $x ; y = $y ")
                if (((x >= (width / 2).toFloat() - 60F) && (x <= (width / 2).toFloat() + 60F))
                    && ((y >= (height / 2).toFloat() - 60F) && (y <= (height / 2).toFloat() + 60F))
                ) {
                    isShowLabel = true
                    invalidate()
                    postDelayed({
                        isShowLabel = false
                        invalidate()
                    }, 10 * 1000)
                }
            }
            else -> {

            }
        }
        return false
    }

    //
//    private fun addDot() {
//        val dotView = DotView(mContext)
//        val layoutParam = ViewGroup.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        dotView.x = (width / 2).toFloat()
//        dotView.y = (height / 2).toFloat()
//        dotView.layoutParams = layoutParam
//        addView(dotView, layoutParam)
//    }

}