package com.inz.z.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.support.v7.widget.TintTypedArray
import android.util.AttributeSet
import android.view.View
import com.inz.z.R
import com.inz.z.util.Tools


/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/3 9:20.
 */
class CircleProgressView : View {

    /**
     * 圆环宽度
     */
    private var circleWidth: Float = 4F
    /**
     * 圆环大小
     */
    private var circleSize: Int? = 40

    /**
     * 圆环样式
     */
    private var circleStyle: Int? = null

    /**
     * 圆环画笔
     */
    private var circlePaint: Paint? = null

    /**
     * 内圆画笔
     */
    private var insideCirclePaint: Paint? = null

    /**
     * 外圆画笔
     */
    private var outsideCirclePaint: Paint? = null
    /**
     * 进度条画笔
     */
    private var progressPaint: Paint? = null

    /**
     * 内部文字画笔
     */
    private var insideTextPaint: Paint? = null

    /**
     * 进度条矩阵
     */
    private var progressRectF: RectF? = null

    /**
     * 当前进度
     */
    private var currentProgress: Float = 0F
    /**
     * 总进度
     */
    private var allProgress: Float = 100F

    private var mContext: Context? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr

    ) {
        this.mContext = context!!
        setStyle(attrs)
        initPaint()
    }

    private fun initPaint() {
        // 圆环画笔
        circlePaint = Paint()
        // 是否抗锯齿
        circlePaint?.isAntiAlias = true
        // 描边样式
        circlePaint?.style = Paint.Style.STROKE

        // 进度条画笔
        progressPaint = Paint()
        // 填充样式
        progressPaint?.style = Paint.Style.STROKE
        // 扛锯齿
        progressPaint?.isAntiAlias = true

        progressRectF = RectF()

        insideCirclePaint = Paint()
        insideCirclePaint?.isAntiAlias = true
        insideCirclePaint?.style = Paint.Style.STROKE
        insideCirclePaint?.color = ContextCompat.getColor(mContext!!, R.color.card_green_default)
        insideCirclePaint?.strokeWidth = 2F

        outsideCirclePaint = Paint()
        outsideCirclePaint?.isAntiAlias = true
        outsideCirclePaint?.style = Paint.Style.STROKE
        outsideCirclePaint?.color = ContextCompat.getColor(mContext!!, R.color.card_green_default)
        outsideCirclePaint?.strokeWidth = 2F

        insideTextPaint = Paint()
        insideTextPaint?.isAntiAlias = true
//        insideTextPaint?.textAlign = Paint.Align.CENTER
        insideTextPaint?.typeface = Typeface.DEFAULT_BOLD
        insideTextPaint?.textSize = 32F

        val colors: IntArray = intArrayOf(Color.RED, Color.GREEN, Color.GREEN, Color.RED)
        val positions: FloatArray = floatArrayOf(.1F, .5F, .75F, .75F)
        sweepGradient = SweepGradient(
            measuredWidth / 2.toFloat(),
            measuredHeight / 2.toFloat(),
            colors,
            positions
        )

    }

    /**
     * 画笔样式
     */
    var paintStyle: Int? = null

    /**
     * 设置初始化样式
     * @param attrs 样式
     */
    @SuppressLint("RestrictedApi")
    fun setStyle(attrs: AttributeSet?) {
        val typedArray = TintTypedArray.obtainStyledAttributes(
            mContext,
            attrs,
            R.styleable.CircleProgressView,
            0,
            0
        )
        paintStyle = typedArray.getResourceId(
            R.styleable.CircleProgressView_circleStyle,
            R.color.colorPrimary
        )
        currentProgress = typedArray.getFloat(R.styleable.CircleProgressView_currentProgress, 0F)
        allProgress = typedArray.getFloat(R.styleable.CircleProgressView_allProgress, 100F)
        val circleF = typedArray.getDimension(R.styleable.CircleProgressView_circleSize, 60F)
        circleSize = Tools.dp2px(mContext!!, circleF)
        circleWidth = typedArray.getDimension(R.styleable.CircleProgressView_circleWidth, 4F)
        typedArray.recycle()
    }

    private var sweepGradient: SweepGradient? = null


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 水平中心
        val centerWith: Float = (measuredWidth / 2).toFloat()
        // 竖直中心
        val centerHeight: Float = (measuredHeight / 2).toFloat()
        // 圆环半径
        val circleRadius: Float = Tools.dp2px(mContext!!, (circleSize!! / 2).toFloat()).toFloat()
        val cWidth: Float = Tools.dp2px(mContext!!, circleWidth).toFloat()

        canvas?.drawCircle(
            centerWith,
            centerHeight,
            circleRadius - cWidth / 2,
            insideCirclePaint!!
        )

        canvas?.drawCircle(
            centerWith,
            centerHeight,
            circleRadius + cWidth / 2,
            outsideCirclePaint!!
        )

        circlePaint?.color = ContextCompat.getColor(mContext!!, R.color.card_white_default)
        circlePaint?.strokeWidth = cWidth

//        circlePaint?.shader = sweepGradient

        canvas?.drawCircle(centerWith, centerHeight, circleRadius, circlePaint!!)

        progressPaint?.color = paintStyle!!
        progressPaint?.strokeWidth = cWidth - 1

        progressRectF?.set(
            centerWith - circleRadius,
            centerHeight - circleRadius,
            centerWith + circleRadius,
            centerHeight + circleRadius
        )

        progressPaint?.shader = sweepGradient
        val startAngle: Float = currentProgress / allProgress * 360
        canvas?.drawArc(progressRectF!!, -90F, 1F + startAngle, false, progressPaint!!)

        val progressNum = currentProgress / allProgress * 100
        val textNum = String.format("%.2f", progressNum) + "%"
        // 获取文字 宽度
        val textWidth = insideTextPaint!!.measureText(textNum)
        val fontMetrics = insideTextPaint?.fontMetrics
        // 获取文字的 baseline
        val baseline = (fontMetrics!!.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
        canvas?.drawText(
            textNum,
            centerWith - textWidth / 2,
            centerHeight + baseline,
            insideTextPaint!!
        )

        if (allProgress > currentProgress) {
            currentProgress += .1F
            postInvalidateDelayed(10)
        } else {
            currentProgress = 100F
            progressPaint?.shader = null
            progressPaint?.color = ContextCompat.getColor(mContext!!, R.color.card_green_default)
            canvas?.drawArc(progressRectF!!, -90F, 1F + startAngle, false, progressPaint!!)
        }
    }

}