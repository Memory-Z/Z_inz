package com.inz.z.face_module.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.cameraview.AspectRatio
import com.google.android.cameraview.CameraView
import com.inz.z.face_module.view.widget.Camera2SurfaceView
import com.inz.z.face_module.view.widget.CameraSurfaceView
import com.inz.z.face_module.view.widget.FaceCamera2Layout

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/05/27 11:57.
 */
@Route(path = "/face/cameraSurface", group = "face")
class CameraSurfaceActivity : AppCompatActivity() {
    private var mCameraSurfaceView: CameraSurfaceView? = null
    private var mCameraView: CameraView? = null
    private var mCamera2SurfaceView: Camera2SurfaceView? = null
    private var mFaceCamera2Layout: FaceCamera2Layout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mCameraView = CameraView(this)
        mCamera2SurfaceView = Camera2SurfaceView(this)
        mFaceCamera2Layout = FaceCamera2Layout(this)
        setContentView(mFaceCamera2Layout)
    }

    override fun onResume() {
        super.onResume()
        mCameraView?.facing = CameraView.FACING_BACK
        mCameraView?.setAspectRatio(AspectRatio.of(9, 16))
        mCameraView?.start()
        mFaceCamera2Layout?.start()
    }

    override fun onPause() {
        super.onPause()
        mCameraView?.stop()
        mFaceCamera2Layout?.stop()
    }
}