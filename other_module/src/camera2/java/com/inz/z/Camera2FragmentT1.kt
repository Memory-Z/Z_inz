package com.inz.z

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.inz.z.other_module.R

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/29 14:30.
 */
class Camera2FragmentT1 : Fragment() {

    companion object {
        fun newInstance() = Camera2FragmentT1()
        const val TAG = "Camera2FragmentT1"
    }

    private lateinit var recordBtn: Button
    private lateinit var captureBtn: Button
    private lateinit var textureView: AutoFitTextureView

    private var mainHandlerThread: HandlerThread? = null
    private var mainHandler: Handler? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.camera2_fragment_t1, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recordBtn = view.findViewById(R.id.camera_fragment_record_btn)
        recordBtn.setOnClickListener {

        }
        captureBtn = view.findViewById(R.id.camera_fragment_capture_btn)
        captureBtn.setOnClickListener {

        }
        textureView = view.findViewById(R.id.camera_fragment_texture)

    }


    override fun onResume() {
        super.onResume()

        startBackgroundThread()

        if (textureView.isAvailable) {

        }

    }

    override fun onPause() {
        stopBackgroundThread()
        super.onPause()

    }

    private fun startBackgroundThread() {
        mainHandlerThread = HandlerThread("Camera2FragmentT1")
        mainHandlerThread?.start()
        mainHandler = Handler(mainHandlerThread?.looper)
    }

    private fun stopBackgroundThread() {
        mainHandlerThread?.quitSafely()
        try {
            mainHandlerThread?.join()
            mainHandlerThread = null
            mainHandler = null
        } catch (e: InterruptedException) {
            Log.e(TAG, e.toString())
        }
    }




    private fun openCamera(width: Int, height: Int) {
        // TODO 已经获取到相机权限

        val cameraManager = activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager



    }
}
