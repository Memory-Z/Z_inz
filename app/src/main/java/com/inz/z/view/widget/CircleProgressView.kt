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
    private var circleWidth: Int = 4
    /**
     * 圆环大小
     */
    private var circleSize: Int = 40
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
    // 默认中心位置
    private val defaultX: Float = 360F
    private val defaultY: Float = 360F
    private var sweepMatrix: Matrix? = null

    private var sweepGradient: SweepGradient? = null

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

    /**
     * 初始化 画笔
     */
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
        insideTextPaint?.textSize = 36F

        val colors: IntArray = intArrayOf(Color.RED, Color.GREEN, Color.GREEN, Color.RED)
        val positions: FloatArray = floatArrayOf(.1F, .5F, .75F, .85F)
        sweepGradient = SweepGradient(
            defaultX,
            defaultY,
            colors,
            positions
        )
        sweepMatrix = Matrix()
        sweepMatrix?.postRotate(-90F, defaultX, defaultY)
    }

    /**
     * 画笔样式
     */
    private var paintStyle: Int? = null

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
        val circleWF = typedArray.getDimension(R.styleable.CircleProgressView_circleWidth, 4F)
        circleWidth = Tools.dp2px(mContext!!, circleWF)
        circleStyle = typedArray.getResourceId(
            R.styleable.CircleProgressView_circleStyle,
            R.style.AppTheme_ProgressHorStyle
        )
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false))
    }

    /**
     * 默认宽度
     */
    private val defaultWidth: Int = 20

    /**
     * 测量
     * @param origin 原始数据
     * @param isWidth 是否为宽
     * @return 计算后的结果
     */
    private fun measure(origin: Int, isWidth: Boolean): Int {
        var result: Int = defaultWidth
        // 获取模式
        val specMode = MeasureSpec.getMode(origin)
        // 获取大小
        val specSize = MeasureSpec.getSize(origin)
        when (specMode) {
            //EXACTLY是精确尺寸，当我们将控件的layout_width或layout_height指定为具体数值时;
            // 如android:layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
            View.MeasureSpec.EXACTLY,
                //AT_MOST是最大尺寸，当控件的layout_width或layout_height指定为WRAP_CONTENT时，
                // 控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
            View.MeasureSpec.AT_MOST -> {
                result = specSize
                if (isWidth) {
                    //  widthForUnspecified = result;
                } else {
                    //  heightForUnspecified = result;
                }
            }
            //UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，通过measure方法传入的模式。
            View.MeasureSpec.UNSPECIFIED -> {
                result = Math.min(result, specSize)
                if (isWidth) {//宽或高未指定的情况下，可以由另一端推算出来 - -如果两边都没指定就用默认值
                    // result = (int) (heightForUnspecified * BODY_WIDTH_HEIGHT_SCALE);
                } else {
                    // result = (int) (widthForUnspecified / BODY_WIDTH_HEIGHT_SCALE);
                }
                if (result == 0) {
                    result = defaultWidth
                }
            }
            else -> {
                result = Math.min(result, specSize)
                if (isWidth) {
                    //宽或高未指定的情况下，可以由另一端推算出来 - -如果两边都没指定就用默认值
                    // result = (int) (heightForUnspecified * BODY_WIDTH_HEIGHT_SCALE);
                } else {
                    // result = (int) (widthForUnspecified / BODY_WIDTH_HEIGHT_SCALE);
                }
                if (result == 0) {
                    result = defaultWidth
                }
            }
        }
        return result
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 水平中心
        val centerWith: Float = (measuredWidth / 2).toFloat()
        // 竖直中心
        val centerHeight: Float = (measuredHeight / 2).toFloat()
        // 圆环半径
        val circleRadius: Float = circleSize / 2.toFloat()
        val cWidth: Float = circleWidth.toFloat()

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

//        sweepMatrix?.setTranslate(centerWith - defaultX, centerHeight - defaultY);
        sweepMatrix?.postTranslate(centerWith - defaultX, centerHeight - defaultY)
        sweepGradient?.setLocalMatrix(matrix)

        progressPaint?.shader = sweepGradient
        val startAngle: Float = currentProgress / allProgress * 360
        canvas?.drawArc(progressRectF!!, -90F, 1F + startAngle, false, progressPaint!!)

        var progressNum = currentProgress / allProgress * 100
        progressNum = if (progressNum > 100F) 100F else progressNum
        val textNum = String.format("%.2f", progressNum) + "%"
        // 获取文字 宽度
        val textWidth = insideTextPaint!!.measureText(textNum)
        val fontMetrics = insideTextPaint!!.fontMetrics
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