package com.inz.z.face_module.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.hardware.camera2.params.Face
import android.media.FaceDetector
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.view.Gravity
import android.view.SurfaceHolder
import android.view.TextureView
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/05/30 09:24.
 */
class FaceCamera2Layout : LinearLayout {
    companion object {
        private const val TAG = "FaceCamera2Layout"
    }

    private var mContext: Context? = null
    private var mFaceCamera2Device: CameraDevice? = null
    private var mFaceCamera2HandlerThread: HandlerThread? = null
    private var mFaceCamera2Handler: Handler? = null
    private var mFaceCamera2Manager: CameraManager? = null
    private var mFaceCamera2Characteristics: CameraCharacteristics? = null
    private var mFaceCamera2Session: CameraCaptureSession? = null
    private var mImageReader: ImageReader? = null
    private var mFaceCamera2Id = "1"
    private var degree: Int? = 0
    private var mFaceCamera2PreviewBuilder: CaptureRequest.Builder? = null
    private var mFaceCamera2SurfaceView: FaceSurfaceView? = null
    private var mFaceCamera2SurfaceHolder: SurfaceHolder? = null
    private lateinit var mFaceTextureView: TextureView


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mContext = context
        initView()
    }

    private fun initView() {
        if (mFaceCamera2SurfaceView == null) {
            mFaceCamera2SurfaceView = FaceSurfaceView(mContext)
            val lp = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            lp.gravity = Gravity.CENTER
            addView(mFaceCamera2SurfaceView, lp)
            mFaceTextureView = TextureView(mContext)
            addView(mFaceTextureView)
        }
        mFaceCamera2SurfaceHolder = mFaceCamera2SurfaceView?.holder
    }

    /**
     * SurfaceHolder 回调实现
     */
    private inner class FaceCamera2SurfaceHolderCallbackImpl : SurfaceHolder.Callback2 {
        override fun surfaceRedrawNeeded(holder: SurfaceHolder?) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun surfaceCreated(holder: SurfaceHolder?) {
            initCamera2AndPreview()
        }
    }

    /**
     * 初始化相机 和 预览
     */
    @SuppressLint("MissingPermission")
    private fun initCamera2AndPreview() {
        mFaceCamera2Id = CameraCharacteristics.LENS_FACING_BACK.toString()
        mFaceCamera2HandlerThread = HandlerThread("FaceCamera2Layout")
        mFaceCamera2HandlerThread!!.start()
        mFaceCamera2Handler = Handler(mFaceCamera2HandlerThread!!.looper)
        mFaceCamera2Manager = mContext!!.getSystemService(Context.CAMERA_SERVICE) as CameraManager?;
        mFaceCamera2Characteristics = mFaceCamera2Manager?.getCameraCharacteristics(mFaceCamera2Id)
        // 设置 surface 显示尺寸
        setPreviewSize()

        mFaceCamera2Manager?.openCamera(
            mFaceCamera2Id,
            FaceCamera2DeviceStateCallbackExt(),
            mFaceCamera2Handler
        )
    }

    /**
     * 设置预览大小
     */
    private fun setPreviewSize() {
        val streamMap =
            mFaceCamera2Characteristics?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        degree = mFaceCamera2Characteristics?.get(CameraCharacteristics.SENSOR_ORIENTATION);
        val previewSizes = streamMap?.getOutputSizes(SurfaceHolder::class.java)
        // 摄像头支持的尺寸
        val captureSizes = streamMap?.getOutputSizes(ImageFormat.JPEG)
        if (!previewSizes.isNullOrEmpty()) {
            val scaleSize = getOptimalSizeScale(previewSizes, width, height)
            val size = getOptimalSize(
                previewSizes,
                mFaceCamera2SurfaceView!!.measuredWidth,
                mFaceCamera2SurfaceView!!.measuredHeight
            )
            val lp = mFaceCamera2SurfaceView?.layoutParams
            if (degree == 90 || degree == 270) {
                lp?.width = size.height
                lp?.height = size.width
            } else {
                lp?.width = size.width
                lp?.height = size.height
            }
            mFaceCamera2SurfaceView?.layoutParams = lp
        }

        val maxCaptureSize: Size = captureSizes!!.maxWith(MaxCaptureSizeComparator())!!
        mImageReader = ImageReader.newInstance(
            maxCaptureSize.width,
            maxCaptureSize.height,
            ImageFormat.RGB_565,
            1
        )

        mImageReader?.setOnImageAvailableListener({ it: ImageReader ->
            // 获取拍照数据
            val image = it.acquireLatestImage()
            val buffer = image.planes[0].buffer
            val bytes: ByteArray = ByteArray(buffer.remaining())
            buffer.get(bytes)
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        }, mFaceCamera2Handler)
        initFaceDetect()
    }

    /**
     * 匹配最适合屏幕的尺寸
     */
    private fun getOptimalSize(sizes: Array<Size>, width: Int, height: Int): Size {
        val sizeList: MutableList<Size> = arrayListOf()
        for (option in sizes) {
            if (width > height) {
                if (option.width > width && option.height > height) {
                    sizeList.add(option)
                }
            } else {
                if (option.width > height && option.height > width) {
                    sizeList.add(option)
                }
            }
        }
        if (sizeList.size > 0) {
            if (sizeList.size > 1) {
                return sizeList.minWith(SizeComparator())!!
            } else {
                return sizeList[0]
            }
        }
        return sizes[0]
    }

    /**
     * 适配的尺寸比例
     */
    private fun getOptimalSizeScale(sizes: Array<Size>, width: Int, height: Int): Size {
        val sizeList: MutableList<Size> = arrayListOf()
        // 获取比例相近的
        for (option in sizes) {
            val hs = option.height / previewHeightScale
            val ws = option.width / previewWidthScale
            if (option.height == option.width * previewHeightScale / previewHeightScale
                && option.width >= width && option.height > height
            ) {
                sizeList.add(option)
            }
        }
        if (sizeList.size > 0) {
            return sizeList[0]
        }
        return sizes[0]
    }

    private inner class SizeComparator : Comparator<Size> {
        override fun compare(o1: Size?, o2: Size?): Int {
            if (o1 == null) {
                return -1
            }
            if (o2 == null) {
                return 1
            }
            val v = o1.width * o1.height - o2.width * o2.height
            if (v > 0) {
                return 1
            } else if (v < 0) {
                return -1
            } else {
                return 0
            }
        }
    }

    private inner class MaxCaptureSizeComparator : Comparator<Size> {
        override fun compare(o1: Size?, o2: Size?): Int {
            if (o1 == null) {
                return -1
            }
            if (o2 == null) {
                return 1
            }
            val c = o1.width * o1.height - o2.width * o2.height
            if (c > 0) {
                return 1
            } else if (c < 0) {
                return -1
            } else {
                return 0
            }
        }
    }

    private inner class FaceCamera2DeviceStateCallbackExt : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            mFaceCamera2Device = camera
            mFaceCamera2PreviewBuilder =
                mFaceCamera2Device?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            mFaceCamera2Device?.createCaptureSession(
                listOf(mFaceCamera2SurfaceHolder?.surface, mImageReader?.surface),
                FaceCamera2CaptureStateCallbackExt(),
                mFaceCamera2Handler
            )
        }

        override fun onDisconnected(camera: CameraDevice) {
            mFaceCamera2Device?.close()
            mFaceCamera2Device = null
        }

        override fun onError(camera: CameraDevice, error: Int) {
            Log.e(TAG, "FaceCamera2DeviceStateCallbackExt: onError: error = $error .")
            when (error) {
                ERROR_CAMERA_DEVICE -> {
                    Toast.makeText(mContext!!, "开启相机失败，致名错误-Service", Toast.LENGTH_SHORT).show()
                }
                ERROR_CAMERA_DISABLED -> {
                    Toast.makeText(mContext!!, "开启相机失败，设备设置错误", Toast.LENGTH_SHORT).show()
                }
                ERROR_CAMERA_IN_USE -> {
                    Toast.makeText(mContext!!, "相机正在使用中", Toast.LENGTH_SHORT).show()
                }
                ERROR_CAMERA_SERVICE -> {
                    Toast.makeText(mContext!!, "开启相机失败，致名错误-Service", Toast.LENGTH_SHORT).show()
                }
                ERROR_MAX_CAMERAS_IN_USE -> {
                    Toast.makeText(mContext!!, "开启相机失败，相机使用达最大", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * CameraCaptureSession 状态 回调
     */
    private inner class FaceCamera2CaptureStateCallbackExt : CameraCaptureSession.StateCallback() {
        override fun onConfigureFailed(session: CameraCaptureSession) {
            Log.e(TAG, "FaceCamera2CaptureStateCallbackExt: onConfigureFailed .")
            mFaceCamera2Session = null
        }

        override fun onConfigured(session: CameraCaptureSession) {
            // 摄像头 准备就绪
            mFaceCamera2Session = session
            // 设置相机参数
            mFaceCamera2PreviewBuilder?.addTarget(mFaceCamera2SurfaceHolder!!.surface)
            mFaceCamera2PreviewBuilder?.set(
                CaptureRequest.CONTROL_AF_MODE,
                CameraCharacteristics.CONTROL_AF_MODE_CONTINUOUS_PICTURE
            )
            mFaceCamera2PreviewBuilder?.set(
                CaptureRequest.CONTROL_AE_MODE,
                CameraCharacteristics.CONTROL_AE_MODE_ON_AUTO_FLASH
            )
            mFaceCamera2PreviewBuilder?.set(
                CaptureRequest.CONTROL_AWB_MODE,
                CameraCharacteristics.CONTROL_AWB_MODE_AUTO
            )
            // 人脸识别，返回全部数据信息
            mFaceCamera2PreviewBuilder?.set(
                CaptureRequest.STATISTICS_FACE_DETECT_MODE,
                mFaceDetectMode
            )
            mFaceCamera2PreviewBuilder?.set(
                CaptureRequest.JPEG_ORIENTATION, degree
            )
            // 开始预览
            mFaceCamera2Session?.setRepeatingRequest(
                mFaceCamera2PreviewBuilder!!.build(),
                FaceCamera2CaptureCallbackExt(),
                mFaceCamera2Handler
            )
        }
    }

    private var mFaceDetectMode: Int = CameraCharacteristics.STATISTICS_FACE_DETECT_MODE_OFF

    /**
     * 初始化人脸检测相关信息
     */
    private fun initFaceDetect() {

        val faceDetectCount =
            mFaceCamera2Characteristics?.get(CameraCharacteristics.STATISTICS_INFO_MAX_FACE_COUNT)    //同时检测到人脸的数量
        val faceDetectModes =
            mFaceCamera2Characteristics?.get(CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES)!!  //人脸检测的模式

        mFaceDetectMode = when {
            faceDetectModes.contains(CaptureRequest.STATISTICS_FACE_DETECT_MODE_FULL) -> CaptureRequest.STATISTICS_FACE_DETECT_MODE_FULL
            faceDetectModes.contains(CaptureRequest.STATISTICS_FACE_DETECT_MODE_SIMPLE) -> CaptureRequest.STATISTICS_FACE_DETECT_MODE_FULL
            else -> CaptureRequest.STATISTICS_FACE_DETECT_MODE_OFF
        }

        if (mFaceDetectMode == CaptureRequest.STATISTICS_FACE_DETECT_MODE_OFF) {
            Log.i(TAG, "相机硬件不支持人脸检测")
//            mActivity.toast("相机硬件不支持人脸检测")
            return
        }

        val activeArraySizeRect =
            mFaceCamera2Characteristics?.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE) //获取成像区域
//        val scaledWidth = mPreviewSize.width / activeArraySizeRect?.width()?.toFloat()
//        val scaledHeight = mPreviewSize.height / activeArraySizeRect?.height()?.toFloat()
//        val mirror = mCameraFacing == CameraCharacteristics.LENS_FACING_FRONT
//
//        mFaceDetectMatrix.setRotate(mCameraSensorOrientation.toFloat())
//        mFaceDetectMatrix.postScale(if (mirror) -scaledWidth else scaledWidth, scaledHeight)
//        if (exchangeWidthAndHeight(mDisplayRotation, mCameraSensorOrientation))
//            mFaceDetectMatrix.postTranslate(
//                mPreviewSize.height.toFloat(),
//                mPreviewSize.width.toFloat()
//            )


        Log.i(
            TAG,
            "成像区域  ${activeArraySizeRect!!.width()}  ${activeArraySizeRect.height()} 比例: ${activeArraySizeRect.width().toFloat() / activeArraySizeRect.height()}"
        )
//        log("预览区域  ${mPreviewSize.width}  ${mPreviewSize.height} 比例 ${mPreviewSize.width.toFloat() / mPreviewSize.height}")

        for (mode in faceDetectModes) {
            Log.i(TAG, "支持的人脸检测模式 $mode")
        }
        Log.i(TAG, "同时检测到人脸的数量 $faceDetectCount")
    }

    /**
     * CameraCaptureSession 照片回调
     */
    private inner class FaceCamera2CaptureCallbackExt : CameraCaptureSession.CaptureCallback() {
        override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult
        ) {
            super.onCaptureCompleted(session, request, result)
            // 获取的人脸数据
            val faceArrays: Array<Face>? = result.get(CaptureResult.STATISTICS_FACES)
            Log.i(
                TAG,
                "FaceCamera2CaptureCallbackExt: onCaptureCompleted: faceArray.Size = ${faceArrays?.size} . "
            )
//            if (mFaceCamera2SurfaceHolder != null) {
//                val canvas: Canvas = mFaceTextureView.lockCanvas()
//
//            }


        }
    }

    /** =================================== 对外接口 ================================= **/

    private var mFaceCamera2SurfaceHolderCallbackImpl: FaceCamera2SurfaceHolderCallbackImpl? = null

    public fun start() {
        mFaceCamera2SurfaceHolderCallbackImpl = FaceCamera2SurfaceHolderCallbackImpl()
        mFaceCamera2SurfaceHolder?.addCallback(mFaceCamera2SurfaceHolderCallbackImpl)
    }

    /**
     * 停止相机
     */
    public fun stop() {
        mFaceCamera2SurfaceHolder?.removeCallback(mFaceCamera2SurfaceHolderCallbackImpl)
        mFaceCamera2Session?.close()
        mFaceCamera2Session = null
        mFaceCamera2Device?.close()
        mFaceCamera2Device = null
        mImageReader?.close()
        mImageReader = null
    }

    private var previewWidthScale: Int = 9
    private var previewHeightScale: Int = 16
    /**
     * 设置预览比列
     */
    public fun setPreviewScale(width: Int, height: Int) {
        this.previewWidthScale = width
        this.previewHeightScale = height
    }

    /** =================================== 对外接口 ================================= **/

}