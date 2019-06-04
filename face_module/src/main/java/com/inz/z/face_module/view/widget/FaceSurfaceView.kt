package com.inz.z.face_module.view.widget

import android.content.Context
import android.media.FaceDetector
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.SurfaceView

/**
 * + 人脸检测 SurfaceView
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/05/30 09:11.
 */
class FaceSurfaceView : SurfaceView {
    companion object {
        private const val TAG = "FaceSurfaceView"
    }

    private var mContext: Context? = null
    private var mFaceHandler: Handler? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mContext = context
        mFaceHandler = Handler(FaceHandlerCallback())
    }

    private inner class FaceHandlerCallback : Handler.Callback {
        override fun handleMessage(msg: Message?): Boolean {
            return false
        }
    }

    public fun setFaceData(faces: Array<FaceDetector.Face>) {

    }
}