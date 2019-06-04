package com.inz.z.face_module.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import android.widget.Toast
import com.inz.z.face_module.view.google.AutoFitTextureView
import kotlinx.android.synthetic.main.activity_camera.view.*
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/05/31 11:28.
 */
class Camera2Helper(val mActivity: Activity, val mTextureView: AutoFitTextureView) {

    companion object {
        const val TAG = "Camera2Helper"
        const val PREVIEW_WIDTH = 1080                                        //预览的宽度
        const val PREVIEW_HEIGHT = 1440                                       //预览的高度
        const val SAVE_WIDTH = 720                                            //保存图片的宽度
        const val SAVE_HEIGHT = 1280                                          //保存图片的高度
    }

    fun log(msg: String) {
        Log.i(TAG, msg)
    }

    private val mContext: Context = mActivity


    private var mCameraId: String = "0"
    private val mCamera2Manager: CameraManager
    private var mCameraDevice: CameraDevice? = null
    private var mCameraCharacteristics: CameraCharacteristics? = null
    private var mCameraPreviewBuilder: CaptureRequest.Builder? = null
    private var mCameraSensorOrientation: Int = 0
    private var mDisplayRotation = mActivity.windowManager.defaultDisplay.rotation

    private var mImageReader: ImageReader? = null

    private var mSurfaceHolder: SurfaceHolder

    private var mHandlerThread: HandlerThread
    private var mHandler: Handler

    private var mPreviewSize = Size(PREVIEW_WIDTH, PREVIEW_HEIGHT)                      //预览大小
    private var mSavePicSize = Size(SAVE_WIDTH, SAVE_HEIGHT)                            //保存图片大小


    init {
        mCamera2Manager = mContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        mHandlerThread = HandlerThread("Camera2Helper")
        mHandlerThread.start()
        mHandler = Handler(mHandlerThread.looper)
        mCamera2Manager.registerAvailabilityCallback(
            CameraManagerAvailabilityCallbackExt(),
            mHandler
        )
        mTextureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        mSurfaceHolder = mTextureView.surfaceView.holder
        mSurfaceHolder.addCallback(object : SurfaceHolder.Callback2 {
            override fun surfaceRedrawNeeded(holder: SurfaceHolder?) {
                log("SurfaceHolder.Callback2 : surfaceRedrawNeeded: ")
            }

            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
            ) {
                log("SurfaceHolder.Callback2 : surfaceChanged: format = $format ; width = $width ; height = $height")
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                log("SurfaceHolder.Callback2 : surfaceDestroyed: ")
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                log("SurfaceHolder.Callback2 : surfaceCreated: ")
                // 初始化相机
                initCamera()
            }
        })
    }

    /**
     * 初始化相机
     */
    private fun initCamera() {
        val cameraIds = mCamera2Manager.cameraIdList
        if (cameraIds.isNullOrEmpty()) {
            Toast.makeText(mContext, "设备无可用相机", Toast.LENGTH_SHORT).show()
            return
        }
        mCameraId = getNextCameraId(mCamera2Manager, mCameraId) ?: mCameraId
        mCameraCharacteristics = mCamera2Manager.getCameraCharacteristics(mCameraId)
        val isSupportHard = isHardwareLevelSupported(
            mCameraCharacteristics!!,
            CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY
        )
        if (!isSupportHard) {
            Toast.makeText(mContext, "相机硬件不支持新特性", Toast.LENGTH_SHORT).show()
        }
        mCameraSensorOrientation =
            mCameraCharacteristics?.get(CameraCharacteristics.SENSOR_ORIENTATION) ?: 0
        // 获取StreamConfigurationMap，它是管理摄像头支持的所有输出格式和尺寸
        val cameraConfigMap =
            mCameraCharacteristics?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

        // 保存尺寸
        val savePicSize = cameraConfigMap!!.getOutputSizes(ImageFormat.JPEG)
        // 预览尺寸
        val previewSize = cameraConfigMap.getOutputSizes(SurfaceView::class.java)
        // 是否需要切换横竖屏大小
        val exchange = exchangeWidthAndHeight(mDisplayRotation, mCameraSensorOrientation)

        if (exchange) {
            mSavePicSize = getBestSize(
                mSavePicSize.height,
                mSavePicSize.width,
                mSavePicSize.height,
                mSavePicSize.width,
                savePicSize.toList()
            )
            mPreviewSize = getBestSize(
                mPreviewSize.height,
                mPreviewSize.width,
                mTextureView.height,
                mTextureView.width,
                previewSize.toList()
            )
        } else {
            mSavePicSize = getBestSize(
                mSavePicSize.width,
                mSavePicSize.height,
                mSavePicSize.width,
                mSavePicSize.height,
                savePicSize.toList()
            )
            mPreviewSize = getBestSize(
                mPreviewSize.width,
                mPreviewSize.height,
                mTextureView.width,
                mTextureView.height,
                previewSize.toList()
            )
        }
        mTextureView.surfaceTexture.setDefaultBufferSize(mPreviewSize.width, mPreviewSize.height)

        log("预览最优尺寸 ：${mPreviewSize.width} * ${mPreviewSize.height}, 比例  ${mPreviewSize.width.toFloat() / mPreviewSize.height}")
        log("保存图片最优尺寸 ：${mSavePicSize.width} * ${mSavePicSize.height}, 比例  ${mSavePicSize.width.toFloat() / mSavePicSize.height}")

        //根据预览的尺寸大小调整TextureView的大小，保证画面不被拉伸
        val orientation = mActivity.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mTextureView.setAspectRatio(mPreviewSize.width, mPreviewSize.height)
        } else {
            mTextureView.setAspectRatio(mPreviewSize.height, mPreviewSize.width)
        }

        mImageReader =
            ImageReader.newInstance(mPreviewSize.width, mPreviewSize.height, ImageFormat.JPEG, 1)
        mImageReader?.setOnImageAvailableListener({ reader ->
            val image = reader?.acquireNextImage()
            val byteBuffer = image!!.planes[0].buffer
            val byteArray = ByteArray(byteBuffer.remaining())
            byteBuffer.get(byteArray)
            reader.close()
            //                BitmapUtils.savePic(byteArray, mCameraSensorOrientation == 270, { savedPath, time ->
            //                    mActivity.runOnUiThread {
            //                        mActivity.toast("图片保存成功！ 保存路径：$savedPath 耗时：$time")
            //                    }
            //                }, { msg ->
            //                    mActivity.runOnUiThread {
            //                        mActivity.toast("图片保存失败！ $msg")
            //                    }
            //                })
        }, mHandler)


        // 打开摄像头
        openCamera(mCameraId)
    }

    /**
     * Returns true if the device supports the required hardware level, or better.
     */
    private fun isHardwareLevelSupported(c: CameraCharacteristics, requiredLevel: Int): Boolean {
        val sortedHwLevel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            arrayOf(
                CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY,
                CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_EXTERNAL,
                CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED,
                CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL,
                CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_3
            )
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                arrayOf(
                    CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY,
                    CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED,
                    CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL,
                    CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_3
                )
            } else {
                arrayOf(
                    CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY,
                    CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED,
                    CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL
                )
            }
        }

        val deviceLevel = c.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)
        if (requiredLevel == deviceLevel) {
            return true
        }
        for (sortedLevel in sortedHwLevel) {
            if (sortedLevel == requiredLevel) {
                return true
            } else if (sortedLevel == deviceLevel) {
                return false
            }
        }
        return false
    }

    /**
     * 根据提供的屏幕方向 [displayRotation] 和相机方向 [sensorOrientation] 返回是否需要交换宽高
     */
    private fun exchangeWidthAndHeight(displayRotation: Int, sensorOrientation: Int): Boolean {
        var exchange = false
        when (displayRotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 ->
                if (sensorOrientation == 90 || sensorOrientation == 270) {
                    exchange = true
                }
            Surface.ROTATION_90, Surface.ROTATION_270 ->
                if (sensorOrientation == 0 || sensorOrientation == 180) {
                    exchange = true
                }
            else -> log("Display rotation is invalid: $displayRotation")
        }

        log("屏幕方向  $displayRotation")
        log("相机方向  $sensorOrientation")
        return exchange
    }

    /**
     *
     * 根据提供的参数值返回与指定宽高相等或最接近的尺寸
     *
     * @param targetWidth   目标宽度
     * @param targetHeight  目标高度
     * @param maxWidth      最大宽度(即TextureView的宽度)
     * @param maxHeight     最大高度(即TextureView的高度)
     * @param sizeList      支持的Size列表
     *
     * @return  返回与指定宽高相等或最接近的尺寸
     *
     */
    private fun getBestSize(
        targetWidth: Int,
        targetHeight: Int,
        maxWidth: Int,
        maxHeight: Int,
        sizeList: List<Size>
    ): Size {
        val bigEnough = ArrayList<Size>()     //比指定宽高大的Size列表
        val notBigEnough = ArrayList<Size>()  //比指定宽高小的Size列表

        for (size in sizeList) {

            //宽<=最大宽度  &&  高<=最大高度  &&  宽高比 == 目标值宽高比
            if (size.width <= maxWidth && size.height <= maxHeight
                && size.width == size.height * targetWidth / targetHeight
            ) {

                if (size.width >= targetWidth && size.height >= targetHeight)
                    bigEnough.add(size)
                else
                    notBigEnough.add(size)
            }
            log("系统支持的尺寸: ${size.width} * ${size.height} ,  比例 ：${size.width.toFloat() / size.height}")
        }

        log("最大尺寸 ：$maxWidth * $maxHeight, 比例 ：${targetWidth.toFloat() / targetHeight}")
        log("目标尺寸 ：$targetWidth * $targetHeight, 比例 ：${targetWidth.toFloat() / targetHeight}")

        //选择bigEnough中最小的值  或 notBigEnough中最大的值
        return when {
            bigEnough.size > 0 -> Collections.min(bigEnough, CompareSizesByArea())
            notBigEnough.size > 0 -> Collections.max(notBigEnough, CompareSizesByArea())
            else -> sizeList[0]
        }
    }

    private class CompareSizesByArea : Comparator<Size> {
        override fun compare(size1: Size, size2: Size): Int {
            return java.lang.Long.signum(size1.width.toLong() * size1.height - size2.width.toLong() * size2.height)
        }
    }

    /**
     * 开启摄像头
     */
    @SuppressLint("MissingPermission")
    private fun openCamera(cameraId: String) {
        try {
            mCamera2Manager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    mCameraDevice = camera
                    // 设置预览配置
                    setPreviewConfig()
                }

                override fun onDisconnected(camera: CameraDevice) {
                    mCameraDevice?.close()
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    log("openCamera: CameraDevice.StateCallback: onError: error = $error .")

                }
            }, mHandler)
        } catch (e: Exception) {
            log("openCamera: Exception")
        }
    }

    /**
     * 设置预览配置
     */
    private fun setPreviewConfig() {
        mCameraPreviewBuilder =
            mCameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        mCameraPreviewBuilder?.addTarget(mSurfaceHolder.surface)
    }


    /**
     * 获取对应的相机 ID 列表
     * @param cameraIds 相机 Id 数组
     * @param cameraManager CameraManager
     * @param facing 需要获取 的相机属性
     * @return 对应的相机
     */
    private fun filterCameraIdsFacing(
        cameraIds: Array<String>,
        cameraManager: CameraManager,
        facing: Int
    ): List<String> {
        return cameraIds.filter {
            val cameraCharacteristics = cameraManager.getCameraCharacteristics(it)
            cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == facing
        }
    }

    /**
     * 获取相机对应的ID
     * @param cameraManager CameraManager
     * @param cameraId 摄像头ID
     * @return 获取到的摄像头 ID
     */
    private fun getNextCameraId(cameraManager: CameraManager, cameraId: String? = null): String? {
        // Get all front, back and external cameras in 3 separate lists
        val cameraIds = cameraManager.cameraIdList
        val backCameraIds =
            filterCameraIdsFacing(cameraIds, cameraManager, CameraCharacteristics.LENS_FACING_BACK)
        val frontCameraIds =
            filterCameraIdsFacing(cameraIds, cameraManager, CameraCharacteristics.LENS_FACING_FRONT)

        val externalCameraIds = listOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            filterCameraIdsFacing(
                cameraIds,
                cameraManager,
                CameraCharacteristics.LENS_FACING_EXTERNAL
            )
        }
        // The recommended order of iteration is: all external, first back, first front
        val allCameras = (externalCameraIds + listOf(
            backCameraIds.firstOrNull(),
            frontCameraIds.firstOrNull()
        )).filterNotNull()
        // Get the index of the currently selected camera in the list
        val cameraIdIndex = allCameras.indexOf(cameraId)
        // The selected camera may not be on the list, for example it could be an
        // external camera that has been removed by the user
        return if (cameraIdIndex == -1) {
            // Return the first camera from the list
            allCameras.getOrNull(0)
        } else {
            // Return the next camera from the list, wrap around if necessary
            allCameras.getOrNull((cameraIdIndex + 1) % cameraIds.size)
        }
    }

    /**
     * 切换摄像头
     */
    fun switchCamera() {
        mCameraId = getNextCameraId(mCamera2Manager, mCameraId) ?: mCameraId
        // TODO 切换摄像头
    }

    /**
     * 摄像头朝向
     */
    enum class Facing {
        BREAK,
        EXTERNAL,
        FRONT
    }

    private var mFacing: Facing = Facing.BREAK

    /**
     * 切换摄像头朝向
     * @param facing 摄像头 朝向 ：{@link #Facing}
     *
     */
    fun switchCameraFacing(facing: Facing) {
        var cameraId = mCameraId
        when (facing) {
            Facing.BREAK -> {
                cameraId = CameraMetadata.LENS_FACING_BACK.toString()
            }
            Facing.EXTERNAL -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    cameraId = CameraMetadata.LENS_FACING_EXTERNAL.toString()
                }
            }
            Facing.FRONT -> {
                cameraId = CameraMetadata.LENS_FACING_FRONT.toString()
            }
        }
        mCameraId = getNextCameraId(mCamera2Manager, cameraId) ?: mCameraId
        // TODO 切换摄像头 处理

    }

    /* =========================== CameraManager.AvailabilityCallback ======================== */
    /**
     * 相机可用状态回调实现
     */
    private inner class CameraManagerAvailabilityCallbackExt :
        CameraManager.AvailabilityCallback() {

        override fun onCameraUnavailable(cameraId: String) {
            super.onCameraUnavailable(cameraId)
        }

        override fun onCameraAvailable(cameraId: String) {
            super.onCameraAvailable(cameraId)
        }
    }


}