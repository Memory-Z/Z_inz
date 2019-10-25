package com.inz.z.camera_one.view.activity

import android.util.Size
import android.view.TextureView
import android.widget.Toast
import androidx.camera.core.*
import androidx.lifecycle.LifecycleOwner
import com.inz.z.base.util.BaseTools
import com.inz.z.base.util.FileUtils
import com.inz.z.base.util.L
import com.inz.z.base.view.AbsBaseActivity
import com.inz.z.camera_one.R
import kotlinx.android.synthetic.main.camera_x_layout.*
import java.io.File
import java.util.concurrent.Executor

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/21 11:54.
 */
class CameraXFragmentActivity : AbsBaseActivity() {

    companion object {
        const val TAG = "CameraXFragmentActivity"
    }

    private lateinit var textureView: TextureView
    private lateinit var imageCapture: ImageCapture
    private lateinit var imageAnalysis: ImageAnalysis
    private lateinit var preview: Preview

    override fun initWindow() {

    }

    override fun getLayoutId(): Int {
        return R.layout.camera_x_layout
    }

    override fun initView() {
        textureView = findViewById(R.id.camera_x_texture_view)
        initCameraX(textureView)
        camera_x_btn.setOnClickListener {
            L.i(TAG, "on Click --- . ")
            val file =
                File(FileUtils.getCacheFilePath(mContext) + File.separator + System.currentTimeMillis() + ".jpeg")
            if (!file.parentFile!!.exists()) {
                file.parentFile!!.mkdirs()
            }
            imageCapture.takePicture(file,
                {
                    L.i(TAG, " ------- ${System.currentTimeMillis()} . ----> ${file.absolutePath}  --- ${Thread.currentThread().name}")
                },
                object : ImageCapture.OnImageSavedListener {
                    override fun onImageSaved(file: File) {
                        L.i(TAG, "onImageSaved --- . ${file.absolutePath}")
                        Toast.makeText(mContext, "拍照完成", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(
                        imageCaptureError: ImageCapture.ImageCaptureError,
                        message: String,
                        cause: Throwable?
                    ) {
                        L.e(TAG, "onError --- . $message", cause)
                    }
                }
            )
        }
    }

    override fun initData() {
    }

    /**
     * 初始化相机 CameraX
     */
    private fun initCameraX(texture: TextureView) {
        val imageCaptureConfig = ImageCaptureConfig.Builder()
            .setTargetRotation(windowManager.defaultDisplay.rotation)
            .setCaptureMode(ImageCapture.CaptureMode.MAX_QUALITY)
            .setLensFacing(CameraX.LensFacing.BACK)
            .build()
        imageCapture = ImageCapture(imageCaptureConfig)

        val imageAnalysisConfig = ImageAnalysisConfig.Builder()
            .setLensFacing(CameraX.LensFacing.BACK)
            .setTargetResolution(Size(720, 1280))
            .build()

        imageAnalysis = ImageAnalysis(imageAnalysisConfig)
        imageAnalysis.run {
            setAnalyzer(
                { executor ->
                    L.i(TAG, " ---> $executor")
                },
                { image, rotationDegrees ->
                    L.i(TAG, "----> ${image.width} --- $rotationDegrees")
                }
            )
        }

        val previewConfig = PreviewConfig.Builder()
            .setLensFacing(CameraX.LensFacing.BACK)
            .build()

        preview = Preview(previewConfig)

        preview.setOnPreviewOutputUpdateListener { output ->
            texture.surfaceTexture = output.surfaceTexture
        }
        CameraX.bindToLifecycle(this as LifecycleOwner, imageCapture, imageAnalysis, preview)

    }
}