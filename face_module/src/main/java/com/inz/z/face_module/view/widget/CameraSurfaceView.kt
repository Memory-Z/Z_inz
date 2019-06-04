package com.inz.z.face_module.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.hardware.camera2.*
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.util.SparseIntArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.inz.z.face_module.R
import com.inz.z.face_module.entity.Constants
import java.util.concurrent.ScheduledThreadPoolExecutor

/**
 * 相机 SurfaceView
 * 由于当前最低 api 支持 21 ,使用 Camera2
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/05/27 11:00.
 */
open class CameraSurfaceView : SurfaceView {
    companion object {
        private const val TAG = "CameraSurfaceView"
        private val INTERNAL_FACINGS = SparseIntArray().apply {
            put(Constants.FACING_BACK, CameraCharacteristics.LENS_FACING_BACK)
            put(Constants.FACING_FRONT, CameraCharacteristics.LENS_FACING_FRONT)
        }
    }

    private var mContext: Context? = null
    private var mSurfaceHolder: SurfaceHolder? = null
    private var mCanvas: Canvas? = null
    private var isDrawing: Boolean = false
    private var threadPollExecutor: ScheduledThreadPoolExecutor? = null
    /**
     * Camera2 摄像头管理器
     */
    private var mCameraManager: CameraManager? = null
    /**
     * 系统摄像头 类似于早期的 Camera
     */
    private var mCameraDevice: CameraDevice? = null
    private var mCameraCharacteristics: CameraCharacteristics? = null
    private var mPreviewRequestBuilder: CaptureRequest.Builder? = null
    private var mCaptureSession: CameraCaptureSession? = null
    private var mHandler: Handler? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        mContext = context
        mHandler = Handler()
        initView()
    }


    /**
     * 初始化 布局
     */
    private fun initView() {
        this.isFocusable = true
        this.isFocusableInTouchMode = true
        mSurfaceHolderCallback2 = SurfaceHolderCallback()
        mSurfaceHolder = holder
        mSurfaceHolder?.addCallback(mSurfaceHolderCallback2)
        mSurfaceHolder?.setKeepScreenOn(true)

        threadPollExecutor = ScheduledThreadPoolExecutor(1)
    }

    private var mSurfaceHolderCallback2: SurfaceHolderCallback? = null

    /**
     * SurfaceView Callback2 实现
     */
    inner class SurfaceHolderCallback : SurfaceHolder.Callback2 {
        override fun surfaceRedrawNeeded(holder: SurfaceHolder?) {
            Log.i(TAG, "surfaceRedrawNeeded - ")
        }

        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            Log.i(TAG, "surfaceChanged - format = $format ; width = $width ; height = $height")
        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
            isDrawing = false
            Log.i(TAG, "surfaceDestroyed - ")
        }

        override fun surfaceCreated(holder: SurfaceHolder?) {
            isDrawing = true
            val drawRunnable = DrawRunnable()
            threadPollExecutor?.execute(drawRunnable)
            Log.i(TAG, "surfaceCreated - ")
        }
    }

    /**
     * 绘制线程
     */
    inner class DrawRunnable : Runnable {
        override fun run() {
            while (isDrawing) {
                val startTime = System.currentTimeMillis()
                mSurfaceHolder?.let {
                    synchronized(it) {
                        try {
                            mCanvas = mSurfaceHolder?.lockCanvas()
                            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.face_0)
                            mCanvas?.drawBitmap(bitmap, 0F, 0F, null)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        } finally {
                            if (mCanvas != null) {
                                mSurfaceHolder?.unlockCanvasAndPost(mCanvas)
                            }
                        }

                    }
                }
                val endTime = System.currentTimeMillis()
                val defTime = endTime - startTime
                if (defTime < 24) {
                    Thread.yield()
                }

            }
        }
    }

    /**
     * 初始化相机 Camera2
     */
    @SuppressLint("MissingPermission")
    public fun initCamera2(context: Context?) {
        mCameraManager = context?.getSystemService(Context.CAMERA_SERVICE) as CameraManager?
        val isHaveCameraId = chooseCameraIdByFacing()
        Log.i(TAG, "chooseCameraIdByFacing() - isHaveCameraId = $isHaveCameraId")
        try {
            mCameraManager?.openCamera(mCameraId, CameraDeviceStateCallbackImpl(), mHandler)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var mFacing = 0
    private var mCameraId = "0"

    /**
     * 选择相机
     */
    private fun chooseCameraIdByFacing(): Boolean {
        try {
            val internalFacing = INTERNAL_FACINGS.get(mFacing)
            // 获取摄像头列表
            val ids = mCameraManager?.cameraIdList
            if (ids.isNullOrEmpty()) {
                throw RuntimeException("No camera available.")
            }
            for (id in ids) {
                val cameraCharacteristics = mCameraManager?.getCameraCharacteristics(id)
                val level =
                    cameraCharacteristics?.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)
                if (level == null || level == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
                    continue
                }
                // 获取摄像头方向
                val internal = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)
                    ?: throw NullPointerException("Unexpected state: LENS_FACING null")
                if (internal == internalFacing) {
                    mCameraId = id
                    mCameraCharacteristics = cameraCharacteristics
                    return true
                }
            }
            // Not found
            mCameraId = ids[0]
            val cameraCharacteristics = mCameraManager?.getCameraCharacteristics(mCameraId)
            val level =
                cameraCharacteristics?.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)
            if (level == null || level == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
                return false
            }
            val internal = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)
                ?: throw NullPointerException("Unexpected state: LENS_FACING null")
            for (i in 0 until INTERNAL_FACINGS.size()) {
                if (INTERNAL_FACINGS[i] == internal) {
                    mFacing = INTERNAL_FACINGS[i]
                    return true
                }
            }
            // The operation can reach here when the only camera device is an external one.
            // We treat it as facing back.
            mFacing = Constants.FACING_BACK
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }


    /**
     * 相机状态回调实现
     */
    inner class CameraDeviceStateCallbackImpl : CameraDevice.StateCallback() {

        override fun onOpened(camera: CameraDevice) {
            mCameraDevice = camera
            // 创建 CameraPreviewSession
            createCameraPreviewSession()
        }

        override fun onDisconnected(camera: CameraDevice) {
            camera.close()
            mCameraDevice = null
        }

        override fun onError(camera: CameraDevice, error: Int) {
            camera.close()
            mCameraDevice = null
        }
    }

    /**
     * 相机是否开启
     */
    private fun isCameraOpened(): Boolean {
        return mCameraDevice != null
    }

    /**
     * 创建新的 CameraCaptureSession
     */
    private fun createCameraPreviewSession() {
        try {
            mPreviewRequestBuilder =
                mCameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            mPreviewRequestBuilder?.addTarget(mSurfaceHolder!!.surface)
            // 创建一个CameraCaptureSession 进行相机预览
            mCameraDevice?.createCaptureSession(
                listOf(mSurfaceHolder!!.surface),
                CameraCaptureSessionStateCallbackImpl(),
                null
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 相机预览状态回调实现
     */
    inner class CameraCaptureSessionStateCallbackImpl : CameraCaptureSession.StateCallback() {
        override fun onConfigureFailed(session: CameraCaptureSession) {
            Log.e(TAG, "onConfigureFailed - Failed to configure capture session.")
        }

        override fun onConfigured(session: CameraCaptureSession) {
            // 相机是否开启
            if (!isCameraOpened()) {
                return
            }
            // 会话准备好后，开始显示预览
            mCaptureSession = session
            updateAutoFocus()
            updateFlash()
            try {
                mCaptureSession?.setRepeatingRequest(
                    mPreviewRequestBuilder!!.build(),
                    null,
                    mHandler
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onClosed(session: CameraCaptureSession) {
            super.onClosed(session)
            mCaptureSession = null
        }
    }

    /**
     * 相机图片回调
     */
    inner class CameraCaptureSessionCaptureCallbackImpl : CameraCaptureSession.CaptureCallback() {
        override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult
        ) {
            super.onCaptureCompleted(session, request, result)

        }

    }

    private var mAutoFocus = true

    /**
     * 更新对焦状态
     */
    private fun updateAutoFocus() {
        if (mAutoFocus) {
            val modes =
                mCameraCharacteristics?.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES)
            // 是否支持自动对焦
            if (modes == null || modes.isEmpty() || (modes.size == 1 && modes[0] == CameraCharacteristics.CONTROL_AE_ANTIBANDING_MODE_OFF)) {
                mAutoFocus = false
                mPreviewRequestBuilder?.set(
                    CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_OFF
                )
            } else {
                mPreviewRequestBuilder?.set(
                    CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                )
            }
        } else {
            mPreviewRequestBuilder?.set(
                CaptureRequest.CONTROL_AF_MODE,
                CaptureRequest.CONTROL_AF_MODE_OFF
            )
        }
    }

    private var mFlash = Constants.FLASH_OFF

    /**
     * 更新闪光灯状态
     */
    private fun updateFlash() {
        when (mFlash) {
            Constants.FLASH_OFF -> {
                mPreviewRequestBuilder?.set(
                    CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_OFF
                )
                mPreviewRequestBuilder?.set(
                    CaptureRequest.FLASH_MODE,
                    CaptureRequest.FLASH_MODE_OFF
                )
            }
            Constants.FLASH_ON -> {
                mPreviewRequestBuilder?.set(
                    CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH
                )
                mPreviewRequestBuilder?.set(
                    CaptureRequest.FLASH_MODE,
                    CaptureRequest.FLASH_MODE_OFF
                )
            }
            Constants.FLASH_TORCH -> {
                mPreviewRequestBuilder?.set(
                    CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON
                )
                mPreviewRequestBuilder?.set(
                    CaptureRequest.FLASH_MODE,
                    CaptureRequest.FLASH_MODE_TORCH
                )
            }
            Constants.FLASH_AUTO -> {
                mPreviewRequestBuilder?.set(
                    CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
                )
                mPreviewRequestBuilder?.set(
                    CaptureRequest.FLASH_MODE,
                    CaptureRequest.FLASH_MODE_OFF
                )
            }
            Constants.FLASH_RED_EYE -> {
                mPreviewRequestBuilder?.set(
                    CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH_REDEYE
                )
                mPreviewRequestBuilder?.set(
                    CaptureRequest.FLASH_MODE,
                    CaptureRequest.FLASH_MODE_OFF
                )
            }
        }
    }
}