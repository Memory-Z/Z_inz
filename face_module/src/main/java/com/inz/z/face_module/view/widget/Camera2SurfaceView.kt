package com.inz.z.face_module.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/05/29 15:01.
 */
class Camera2SurfaceView : SurfaceView {

    companion object {
        private const val TAG = "Camera2SurfaceView"
        private const val STATUS_PREVIEW = 0
        private const val STATUS_CAPTURE = 1
    }

    private var mCameraManager: CameraManager? = null
    private var mCameraDevice: CameraDevice? = null
    private var mSurfaceHolder: SurfaceHolder? = null
    private var mCameraHandlerThread: HandlerThread? = null
    private var mCameraHandler: Handler? = null
    private var mContext: Context? = null
    private var mCameraId = "1"
    private var mImageReader: ImageReader? = null
    private var mCameraCaptureSession: CameraCaptureSession? = null
    private var mCameraCharacteristics: CameraCharacteristics? = null
    /**
     * 当前状态
     */
    private var mStatus = 0

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mContext = context
        Thread(Runnable {
            initCamera2()
        }).start()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    /**
     * 初始化相机
     */
    private fun initCamera2() {
        mCameraManager = mContext?.getSystemService(Context.CAMERA_SERVICE) as CameraManager?
        mSurfaceHolder = this.holder
        mSurfaceHolder?.addCallback(SurfaceHolderCallbackImpl())
    }

    /**
     * SurfceHolder 回调
     */
    private inner class SurfaceHolderCallbackImpl : SurfaceHolder.Callback2 {
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
            // Surface 创建完成后/初始化相机 和 预览
            initCamera2AndPreview()
        }
    }

    // FixMe 预览照片变形 （分离 Camera 和 View ）

    /**
     * 初始化相机和预览
     */
    @SuppressLint("MissingPermission")
    private fun initCamera2AndPreview() {
        Log.i(TAG, "initCamera2AndPreview: ")
        mCameraHandlerThread = HandlerThread("Camera2")
        mCameraHandlerThread?.start()
        mCameraHandler = Handler(mCameraHandlerThread?.looper)
        try {
            // 获取前摄
            mCameraId = CameraCharacteristics.LENS_FACING_FRONT.toString()
            var idList = mCameraManager?.cameraIdList
            // 设置 ImageReader
            mImageReader = ImageReader.newInstance(
                this.measuredWidth,
                this.measuredHeight,
                ImageFormat.JPEG,
                7
            )
            mCameraCharacteristics = mCameraManager?.getCameraCharacteristics(mCameraId)
            // 获取成像区域
            val size1 =
                mCameraCharacteristics?.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE)
            val size2 =
                mCameraCharacteristics?.get(CameraCharacteristics.JPEG_AVAILABLE_THUMBNAIL_SIZES)
            val size3 = mCameraCharacteristics?.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE)
            // 获取成像尺寸
            val size4 =
                mCameraCharacteristics?.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE)
            val map =
                mCameraCharacteristics?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            // 获取预览尺寸
            val previewSizes = map?.getOutputSizes(SurfaceTexture::class.java)
            // 获取拍照尺寸
            val captureSizes = map?.getOutputSizes(ImageFormat.JPEG)
            // 获取相机角度
            val orientation = mCameraCharacteristics?.get(CameraCharacteristics.SENSOR_ORIENTATION)
            if (orientation != null) {
                this.orientation = orientation
            }
            val faceDetectModes =
                mCameraCharacteristics?.get(CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES)
            val maxFaceCount =
                mCameraCharacteristics?.get(CameraCharacteristics.STATISTICS_INFO_MAX_FACE_COUNT)
            val h = this.measuredHeight
            val w = this.measuredWidth
            if (previewSizes != null) {
                measure(previewSizes[0].width, previewSizes[0].height)
            }
            mCameraManager?.openCamera(mCameraId, CameraDeviceStateCallbackImpl(), mCameraHandler)
        } catch (e: Exception) {
            Log.e(TAG, "initCamera2AndPreview - ", e)
        }
    }


    private inner class CameraDeviceStateCallbackImpl : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            Log.d(TAG, "CameraDeviceStateCallbackImpl onOpened: ")
            mCameraDevice = camera
            createCameraCaptureSession()
        }

        override fun onDisconnected(camera: CameraDevice) {
            Log.d(TAG, "CameraDeviceStateCallbackImpl onDisconnected: ")
            mCameraDevice?.close()
            mCameraDevice = null
        }

        override fun onError(camera: CameraDevice, error: Int) {
            Log.e(TAG, "CameraDeviceStateCallbackImpl onError：Error = $error")
        }
    }

    private var mPreviewBuilder: CaptureRequest.Builder? = null

    /**
     * 创建 CaptureSession
     */
    private fun createCameraCaptureSession() {
        Log.i(TAG, "createCameraCaptureSession: ")
        mPreviewBuilder = mCameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        if (mSurfaceHolder != null) {
            mPreviewBuilder?.addTarget(mSurfaceHolder!!.surface)
            mStatus = STATUS_PREVIEW
            mCameraDevice?.createCaptureSession(
                listOf(
                    mSurfaceHolder!!.surface,
                    mImageReader!!.surface
                ), CameraCaptureSessionStateCallbackExt(), mCameraHandler
            )
        }
    }

    private inner class CameraCaptureSessionStateCallbackExt :
        CameraCaptureSession.StateCallback() {
        override fun onConfigureFailed(session: CameraCaptureSession) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onConfigured(session: CameraCaptureSession) {
            // 摄像头已经准备就绪，开始显示预览
            mCameraCaptureSession = session
            try {
                // 自动对焦
                mPreviewBuilder?.set(
                    CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                )
                // 闪关灯
                mPreviewBuilder?.set(
                    CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_OFF
                )
                // 显示预览
                mCameraCaptureSession?.setRepeatingRequest(
                    mPreviewBuilder!!.build(),
                    CameraCaptureSessionCaptureCallbackExt(),
                    mCameraHandler
                )
            } catch (e: Exception) {
                Log.e(TAG, "mCameraCaptureSession?.setRepeatingRequest  - ", e)
            }
        }
    }

    private inner class CameraCaptureSessionCaptureCallbackExt :
        CameraCaptureSession.CaptureCallback() {
        override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult
        ) {
            super.onCaptureCompleted(session, request, result)
            mCameraCaptureSession = session
        }
    }

    private var orientation = 0

    /**
     * 拍照
     * @param rotation 手机方向
     */
    public fun takePicture() {
        if (mCameraDevice == null) {
            return
        }
        try {
            val captureRequestBuilder =
                mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureRequestBuilder.addTarget(mImageReader!!.surface)
            captureRequestBuilder.set(
                CaptureRequest.CONTROL_AF_MODE,
                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
            )
            captureRequestBuilder.set(
                CaptureRequest.CONTROL_AE_MODE,
                CaptureRequest.CONTROL_AE_MODE_OFF
            )
            // 根据设备方向计算设置照片方向
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, orientation)
            mCameraCaptureSession?.capture(captureRequestBuilder.build(), null, mCameraHandler)
        } catch (e: Exception) {

        }
    }

    public fun pause() {
        mCameraCaptureSession?.close()
        mCameraDevice?.close()
    }
}