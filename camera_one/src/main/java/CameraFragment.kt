//package com.inz.z.camera_one.view.fragment
//
//import android.Manifest
//import android.content.Context
//import android.content.pm.PackageManager
//import android.graphics.SurfaceTexture
//import android.hardware.Camera
//import android.hardware.camera2.CameraManager
//import android.view.Surface
//import android.view.TextureView
//import androidx.core.app.ActivityCompat
//import com.inz.z.base.util.L
//import com.inz.z.base.view.AbsBaseFragment
//
//
///**
// * Camera
// * @author Zhenglj
// * @version 1.0.0
// * Create by inz in 2019/10/17 10:52.
// */
//@Deprecated("Not finish. ")
//class CameraFragment : AbsBaseFragment() {
//    companion object {
//        private const val TAG = "CameraFragment"
//
//        private val PERMISSION_REQUEST_CODE = 0x0002
//        private val PERMISSION_GROUP = arrayOf(
//            Manifest.permission.CAMERA,
//            Manifest.permission.RECORD_AUDIO
//        )
//    }
//
//    private lateinit var camera: Camera
//    private lateinit var textureView: TextureView
//    private var previewSize: Camera.Size? = null
//    private var cameraId = 0
//    private var mSurfaceTexture: SurfaceTexture? = null
//
//    override fun initWindow() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun getLayoutId(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun initView() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun initData() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    /**
//     * 申请相机、录音权限
//     */
//    private fun requestPermission() {
//        if (ActivityCompat.checkSelfPermission(
//                mContext,
//                Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_DENIED
//        ) {
//            requestPermissions(PERMISSION_GROUP, PERMISSION_REQUEST_CODE)
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                L.i(TAG, "Camera permission is granted. ")
//
//            } else {
//                L.w(TAG, "Camera permission is Denied. ")
//            }
//        }
//    }
//
//    private val textureSurfaceListener = object : TextureView.SurfaceTextureListener {
//        override fun onSurfaceTextureSizeChanged(
//            surface: SurfaceTexture?,
//            width: Int,
//            height: Int
//        ) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//
//        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//
//        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//
//        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
//            mSurfaceTexture = surface
//            openCamera(this@CameraFragment.cameraId)
//        }
//    }
//
//    /**
//     * 开启相机
//     */
//    private fun openCamera(cameraId: Int) {
//        if (ActivityCompat.checkSelfPermission(
//                mContext,
//                Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_DENIED
//        ) {
//            requestPermission()
//        } else {
//            val cameraManager = mContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager
//            try {
//                camera = Camera.open(cameraId)
//            } catch (e: Exception) {
//                L.e(TAG, "Camera.open failure. ", e)
//                return
//            }
//        }
//    }
//
//    private fun initCameraParameter(camera: Camera) {
//        val cameraParameter = camera.parameters
//        cameraParameter.jpegQuality = 100
//        requireActivity().requestedOrientation
//
//        val supportPreviewSizes = cameraParameter.supportedPreviewSizes;
//        previewSize =
//            chooseFitPreviewSize(supportPreviewSizes, textureView.width, textureView.height, true)
//        cameraParameter.setPreviewSize(previewSize!!.width, previewSize!!.height)
//    }
//
//    /**
//     * 获取适配的预览大小
//     * @param supportPreviewSizes   相机支持的大小
//     * @param previewWidth          预览宽
//     * @param previewHeight         预览高
//     * @param rotate                是否旋转
//     */
//    private fun chooseFitPreviewSize(
//        supportPreviewSizes: List<Camera.Size>,
//        previewWidth: Int,
//        previewHeight: Int,
//        rotate: Boolean
//    ): Camera.Size {
//        val fitSizes: MutableList<Camera.Size> = mutableListOf()
//        val minFitSize: MutableList<Camera.Size> = mutableListOf()
//        for (size: Camera.Size in supportPreviewSizes) {
//            when (rotate) {
//                true -> {
//                    when (size) {
//                        { size.height / previewWidth == size.width / previewHeight } -> {
//                            if (size.width > previewHeight) {
//                                fitSizes.add(size)
//                            } else {
//                                minFitSize.add(size)
//                            }
//                        }
//                        else -> {
//                        }
//                    }
//                }
//                else -> {
//                    when (size) {
//                        { size.height / previewHeight == size.width / previewWidth } -> {
//                            if (size.width > previewHeight) {
//                                fitSizes.add(size)
//                            } else {
//                                minFitSize.add(size)
//                            }
//                        }
//                        else -> {
//                        }
//                    }
//                }
//            }
//        }
//
//        if (fitSizes.size > 0) {
//            return fitSizes[0]
//        } else {
//            if (minFitSize.size > 0) {
//                return minFitSize[0]
//            } else {
//                return supportPreviewSizes[0]
//            }
//        }
//    }
//
//}