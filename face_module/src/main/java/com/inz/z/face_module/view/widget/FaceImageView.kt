package com.inz.z.face_module.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.media.FaceDetector
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView

/**
 *
 * 人脸识别 框选
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/05/23 16:16.
 */
public open class FaceImageView : ImageView {
    companion object {
        private const val TAG = "FaceImageView"

        /**
         * 最大人脸数量
         */
        private const val MAX_FACE_NUM = 10
    }

    /**
     * 捕捉到的人脸数据
     */
    private var faces: Array<FaceDetector.Face?>? = null
    /**
     * 画笔
     */
    private var paint: Paint? = null
    private var bitmap: Bitmap? = null
    private var xArray: Array<Float?>? = null
    private var yArray: Array<Float?>? = null


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initPaint()
    }

    /**
     * 初始化画笔
     */
    private fun initPaint() {
        paint = Paint()
        paint?.color = Color.GREEN
        paint?.strokeWidth = 4F
        paint?.style = Paint.Style.STROKE
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        val points = IntArray(2)
        getLocationOnScreen(points)
        val isWgtH = bitmap?.width!! >= bitmap?.height!!
        val scaleW = bitmap?.width!! / measuredWidth
        val scaleH = bitmap?.height!! / measuredHeight

        val scaleBitmap = bitmap?.width!!.toDouble() / bitmap?.height!!.toDouble()
        val scaleCanvas = measuredWidth.toDouble() / measuredHeight.toDouble()
        // 判断比例；已 Canvas 未
        val needWorH = scaleBitmap > scaleCanvas
        var hAdd = 0
        var wAdd = 0
        // ImageView 宽是否大于高
        val isViewWgtH = measuredWidth > measuredHeight
        scale = if (needWorH) scaleW else scaleH
        if (scaleType == ScaleType.CENTER_INSIDE || scaleType == ScaleType.FIT_CENTER) {
            if (needWorH) {
                // 宽 大于 高
                if (scaleBitmap > 1.0F) {
                    hAdd = (measuredHeight - bitmap?.height!! / scale) / 2
                } else {
                    wAdd = (measuredWidth - bitmap?.width!! / scale) / 2
                }
            } else {
                // 宽小于高
                if (scaleBitmap > 1.0F) {
                    wAdd = (measuredWidth - bitmap?.width!! / scale) / 2
                } else {
                    hAdd = (measuredHeight - bitmap?.height!! / scale) / 2
                }
            }
//            // 宽 大于 高
//            if (isWgtH) {
//                hAdd = (measuredHeight - bitmap?.height!! / scale) / 2
//            } else {
//                wAdd = (measuredWidth - bitmap?.width!! / scale) / 2
//            }
        }
        wAdd += points[0]
        hAdd += points[1]
        super.onDraw(canvas)
        if (faces.isNullOrEmpty() || xArray.isNullOrEmpty() || yArray.isNullOrEmpty()) {
            return
        }
//        if (bitmap != null) {
//            canvas?.drawBitmap(bitmap!!, 0F, 0F, paint)
//        }
        for (i in 0 until xArray!!.size step 2) {
            val left = xArray!![i] ?: break
            val right = xArray!![i + 1] ?: break
            val top = yArray!![i] ?: break
            val bottom = yArray!![i + 1]
            val rectF = RectF()
            rectF.left = left!! / scale + wAdd
            rectF.top = top!! / scale + hAdd
            rectF.right = right!! / scale + wAdd
            rectF.bottom = bottom!! / scale + hAdd
            canvas?.drawRect(rectF, paint!!)
            canvas?.drawLine(3500F, 824F, 100F, 100F, paint!!)
        }
    }

    private var iw = 1
    private var ih = 1
    private var scale = 1

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        iw = widthMeasureSpec
        ih = heightMeasureSpec
    }


    /**
     * 设置人脸数据
     * @param faces 人脸数据 数组
     * @param bitmap 图片
     */
    public fun setFaces(faces: Array<FaceDetector.Face?>, bitmap: Bitmap) {
        if (!faces.isNullOrEmpty() && faces.isNotEmpty()) {
            Log.e(TAG, "FaceImageView setFaces, face size = " + faces.size)
            xArray = arrayOfNulls(faces.size * 2)
            yArray = arrayOfNulls(faces.size * 2)

            this.faces = faces
            this.bitmap = bitmap
            calculateFaceArea()

            invalidate()
        } else {
            Log.e(TAG, "FaceImageView setFaces, face size = null. ")
        }
    }

    /**
     * 计算人脸区域
     */
    private fun calculateFaceArea() {
        // 两眼间距
        var eyesDistance: Float

        for (i in 0 until faces!!.size) {
            val face = faces!![i] ?: break
            val pointF = PointF()
            // 获取人脸中心点
            face.getMidPoint(pointF)
            // 两眼间距
            eyesDistance = face.eyesDistance()
            // 计算人脸区域
            val delta = eyesDistance / 2
            val leftX = pointF.x - eyesDistance
            xArray!![2 * i] = leftX
            val leftY = pointF.y - eyesDistance
            yArray!![2 * i] = leftY
            val rightX = pointF.x + eyesDistance
            xArray!![2 * i + 1] = rightX
            val rightY = pointF.y + eyesDistance
            yArray!![2 * i + 1] = rightY

        }
    }


}