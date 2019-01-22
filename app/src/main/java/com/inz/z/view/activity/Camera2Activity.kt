package com.inz.z.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.Image
import android.media.ImageReader
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Size
import android.util.SparseIntArray
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.inz.z.R
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.util.*
import kotlin.collections.ArrayList

/**
 * Camera2 页面
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/12/7 17:11.
 */
class Camera2Activity : AppCompatActivity() {

    private var mContext: Context = this

    companion object {
        /**
         * 方向
         */
        private val ORIENTATIONS = SparseIntArray()

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }
    }

    /**
     * 画面填充布局
     */
    private var mTextureView: TextureView? = null
    /**
     * 摄像头ID; (通常"0", 表示后置； "1": 表示前置；多个摄像头 ID 加)
     */
    private var mCameraId = "1"
    /**
     * 系统摄像头 设备
     */
    private var mCameraDevice: CameraDevice? = null
    /**
     * 预览尺寸
     */
    private var previewSize: Size? = null
    /**
     * 预览请求Builder
     */
    private var mPreviewRequestBuilder: CaptureRequest.Builder? = null
    /**
     * 预览请求
     */
    private var mPreviewRequest: CaptureRequest? = null
    /**
     * 捕获 CameraCaptureSession 成员变量
     */
    private var mCaptureSession: CameraCaptureSession? = null
    /**
     * 图片读取
     */
    private var mImageReader: ImageReader? = null
    private var sWidth: Int = 0
    private var sHeight: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 全屏
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_camera2)
        initView()

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        if (sWidth != 0 && sHeight != 0) {
            openCamera(sWidth, sHeight)
        }
    }

    override fun onPause() {
        super.onPause()
        if (mCameraDevice != null) {
            mCaptureSession?.close()
            mCaptureSession = null
            cameraCharacteristics = null
            mCameraDevice?.close()
            mCameraDevice = null
        }
    }


    private var ringRl: RelativeLayout? = null
    private var layoutParams: ViewGroup.LayoutParams? = null

    @SuppressLint("NewApi")
    private fun initView() {
        mTextureView = findViewById(R.id.camera2_texture_v)
        mTextureView?.surfaceTextureListener = TextureViewListener()
        val shootBtn: Button = findViewById(R.id.camera2_shoot)
        shootBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                capturePicture()
            }
        }

        val badgeRing0 = findViewById<ImageView>(R.id.camera2_badge0_iv)

        ringRl = findViewById(R.id.camera2_ring_rl)
        layoutParams = badgeRing0.layoutParams
        badgeRing0.visibility = View.GONE
        var time = 100L
        for (i in 0..6) {
            MyCountDownTimer(time, 100).start()
            time += 200L
        }

    }

    private inner class MyCountDownTimer(future: Long, interval: Long) :
        CountDownTimer(future, interval) {

        override fun onFinish() {
            startAnimation(layoutParams!!)
        }

        override fun onTick(millisUntilFinished: Long) {

        }
    }

    @SuppressLint("NewApi")
    private fun startAnimation(layoutParams: ViewGroup.LayoutParams) {
        val badgeIv = ImageView(mContext)
        badgeIv.background = resources.getDrawable(R.drawable.bg_badge_yellow, null)
        ringRl!!.addView(badgeIv, layoutParams)
        val animation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_ring)
        badgeIv.startAnimation(animation)
    }

    /**
     * TextureView 监听
     */
    private inner class TextureViewListener : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureSizeChanged(
            surface: SurfaceTexture?,
            width: Int,
            height: Int
        ) {
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
            return false
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
            // 当 TextureView 可用时 打开摄像头
            sHeight = height
            sWidth = width
            openCamera(width, height)
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private var myCameraDeviceStateCallback = MyCameraDeviceStateCallback()

    /**
     * 自定义相机状态回调方法
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private inner class MyCameraDeviceStateCallback : CameraDevice.StateCallback() {
        /**
         * 摄像头被打开时 激活该方法
         */
        override fun onOpened(camera: CameraDevice) {
            mCameraDevice = camera
            // 开始预览
            createCameraPreviewSession()
        }

        /**
         * 摄像头断开连接时 激活该方法
         */
        override fun onDisconnected(camera: CameraDevice) {
            if (mCameraDevice != null) {
                mCameraDevice!!.close()
                mCameraDevice = null
            }
        }

        /**
         * 打开摄像头出现错误时 激活该方法
         */
        override fun onError(camera: CameraDevice, error: Int) {
            if (mCameraDevice != null) {
                mCameraDevice!!.close()
                mCameraDevice = null
            }
        }
    }

    /**
     * 打开摄像头
     */
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun openCamera(width: Int, height: Int) {
        // 设置相机 输出
        setUpCameraOutput(width, height)
        // 获取相机管理
        val manager: CameraManager? = getSystemService(Context.CAMERA_SERVICE) as CameraManager?
        try {
            manager?.openCamera(mCameraId, myCameraDeviceStateCallback, null)
        } catch (e: CameraAccessException) {
            println(" --- " + e.message)
        }
    }

    /**
     * 创建预览
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createCameraPreviewSession() {
        try {
            val texture = mTextureView!!.surfaceTexture
            texture?.setDefaultBufferSize(previewSize?.width!!, previewSize?.height!!)
            // 创建作为 预览的 CaptureRequest.Builder
            mPreviewRequestBuilder =
                    mCameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            // 将 textureView 的Surface 作为 CaptureRequest.Builder 的目标
            val surface = Surface(texture)
            mPreviewRequestBuilder?.addTarget(surface)
            // 创建CameraCaptureSession 。 该对象负责管理处理预览请求和拍照请求
            mCameraDevice?.createCaptureSession(
                Arrays.asList(
                    surface,
                    mImageReader?.surface
                ), MyCameraCaptureSessionStateCallback(), null
            )
        } catch (e: CameraAccessException) {
            println(" --- " + e.message)
        }
    }

    /**
     * 自定义CameraCaptureSessionStateCallback
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private inner class MyCameraCaptureSessionStateCallback : CameraCaptureSession.StateCallback() {
        override fun onConfigureFailed(session: CameraCaptureSession) {
            Toast.makeText(mContext, "配置失败", Toast.LENGTH_SHORT).show()
        }

        override fun onConfigured(session: CameraCaptureSession) {
            // 如果摄像头为 null, 直接结束方法
            if (null == mCameraDevice) {
                return
            }
            // 当摄像头已经准备好时，开始显示预览
            mCaptureSession = session
            try {
                // 设置自动对焦模式
                mPreviewRequestBuilder?.set(
                    CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                )
                // 设置自动曝光模式
                mPreviewRequestBuilder?.set(
                    CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
                )
                // 开启显示相机预览
                mPreviewRequest = mPreviewRequestBuilder?.build()
                // 设置预览时连续捕获图像数据
                mCaptureSession?.setRepeatingRequest(mPreviewRequest!!, null, null)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 捕获照片
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun capturePicture() {
        try {
            if (mCameraDevice == null) {
                return
            }
            // 获取 拍照 captureRequest.Builder
            mPreviewRequestBuilder =
                    mCameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            // 将 ImageReader 的Surface 作为 CaptureRequest,Builder 的目标
            mPreviewRequestBuilder?.addTarget(mImageReader!!.surface)
            // 设置自动对焦模式
            mPreviewRequestBuilder?.set(
                CaptureRequest.CONTROL_AF_MODE,
                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
            )
            // 获取设备方向
            val rotation = windowManager.defaultDisplay.rotation
            mPreviewRequestBuilder?.set(
                CaptureRequest.JPEG_ORIENTATION,
//                ORIENTATIONS.get(rotation)
                getJpegOrientation(cameraCharacteristics!!, rotation)
            )
            // 停止连续取景
            mCaptureSession?.stopRepeating()
            // 捕获静态图像
            mCaptureSession?.capture(
                mPreviewRequestBuilder!!.build(),
                MyCameraCaptureSessionCaptureCallback(),
                null
            )
        } catch (e: Exception) {
            println("===>" + e.message)
        }
    }

    /**
     * 获取图像角度
     *
     * @param c                 相机参数
     * @param deviceOrientation 设备方向
     * @return 图片方向
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getJpegOrientation(c: CameraCharacteristics, deviceOrientation: Int): Int {
        var orientation: Int?
        if (deviceOrientation == android.view.OrientationEventListener.ORIENTATION_UNKNOWN)
            return 0
        if (deviceOrientation == 1 && "1" == mCameraId) {
            return 0
        }
        val sensorOrientation = c.get(CameraCharacteristics.SENSOR_ORIENTATION)!!

        // Round device orientation to a multiple of 90
        orientation = (deviceOrientation + 45) / 90 * 90

        // Reverse device orientation for front-facing cameras
        val facingFront =
            c.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT
        if (facingFront) orientation = -deviceOrientation

        // Calculate desired JPEG orientation relative to camera orientation to make
        // the image upright relative to the device orientation
        return (sensorOrientation + orientation + 360) % 360
    }

    /**
     * 自定义 照片回调
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private inner class MyCameraCaptureSessionCaptureCallback :
        CameraCaptureSession.CaptureCallback() {
        /**
         * 拍照完成后 激活
         */
        override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult
        ) {
            super.onCaptureCompleted(session, request, result)
            try {
                // 重新自动对焦模式
                mPreviewRequestBuilder?.set(
                    CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL
                )
                // 设置自动曝光模式
                mPreviewRequestBuilder?.set(
                    CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
                )
                // 打开连续取景模式
                mCaptureSession?.setRepeatingRequest(mPreviewRequest!!, null, null)
            } catch (e: CameraAccessException) {
                println(" --- " + e.message)
            }
        }
    }

    private var cameraCharacteristics: CameraCharacteristics? = null

    /**
     * 设置相机输出
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setUpCameraOutput(width: Int, height: Int) {
        val manager: CameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            // 获取 摄像头特性
            cameraCharacteristics =
                    manager.getCameraCharacteristics(mCameraId)
            // 获取 摄像头配置属性
            val map: StreamConfigurationMap? =
                cameraCharacteristics?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            // 获取摄像头支持的最大尺寸
            val largest: Size = Collections.max(
                Arrays.asList(*map?.getOutputSizes(ImageFormat.JPEG)),
                CompareSizesByArea()
            )
            // 创建一个 ImageReader对象，用户获取摄像头 的图像数据
            mImageReader =
                    ImageReader.newInstance(largest.width, largest.height, ImageFormat.JPEG, 2)
            // 设置 ImageReader 监听
            mImageReader?.setOnImageAvailableListener(ImageAvailableListener(), null)
            // 获取最佳的预览尺寸
            previewSize = chooseOptimalSize(
                map?.getOutputSizes(SurfaceTexture::class.java)!!,
                width,
                height,
                largest
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    /**
     * 为 Size 定义一个比较器 Comparator
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private class CompareSizesByArea : Comparator<Size> {
        override fun compare(o1: Size?, o2: Size?): Int {
            // 强转为 Long 保证不会 发生 溢出
            val result: Long = (o1?.width!! * o1.height - o2?.width!! * o2.height).toLong()
            return when {
                result < 0 -> -1
                result > 0 -> 1
                else -> 0
            }
        }
    }

    /**
     * 图像监听器
     */
    private inner class ImageAvailableListener : ImageReader.OnImageAvailableListener {
        /**
         * 当照片数据可用时，激活该方法
         */
        override fun onImageAvailable(reader: ImageReader?) {
            if (reader != null) {
                saveImageToLocal(reader, localPath!!, "image.jpeg")
            }
        }
    }

    /**
     * 选择最佳尺寸
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun chooseOptimalSize(
        choices: Array<Size>,
        width: Int,
        height: Int,
        aspectRatio: Size
    ): Size {
        // 收集摄像头支持的 大过预览Surface 的分辨率
        val bigEnough = ArrayList<Size>()
        val w = aspectRatio.width
        val h = aspectRatio.height
        for (option: Size in choices) {
            if (option.height == option.width * h / w && option.width >= width && option.height >= height) {
                bigEnough.add(option)
            }
        }
        // 如果 找到 多个预览尺寸，获取其中面积最小的
        return when {
            bigEnough.size > 0 -> Collections.min(bigEnough, CompareSizesByArea())
            else -> choices[0]
        }
    }

    /**
     * 保存图片到本地
     * @param imageReader imageReader 图片读取
     * @param localPath 本地路径（完整路径）
     */
    private fun saveImageToLocal(
        imageReader: ImageReader,
        localPath: String,
        imageName: String
    ): Boolean {
        var flag: Boolean
        val fileDir = File(localPath)
        // 如果不存在，创建文件夹
        flag = if (!fileDir.exists()) {
            fileDir.mkdirs()
        } else {
            true
        }
        val file = File(localPath + File.separator + imageName)
        if (file.exists()) {
            file.delete()
        }
        if (flag) {
            val image: Image = imageReader.acquireLatestImage()
            val buffer: ByteBuffer? = image.planes?.get(0)?.buffer
            val bytes: ByteArray = buffer?.remaining()?.let { ByteArray(it) }!!
            // 使用IO 流将照片写入指定文件
            buffer.get(bytes)
            flag = try {
                val output = FileOutputStream(file)
                output.write(bytes)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            } finally {
                image.close()
            }
        }
        return flag
    }

    /**
     * 默认地址
     */
    private var localPath: String? =
        Environment.getExternalStorageDirectory().absolutePath + File.separator + "Inz" + File.separator
}
