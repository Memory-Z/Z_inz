package com.inz.z.camera_one.view.fragment

import android.hardware.display.DisplayManager
//import androidx.camera.core.*
import com.inz.z.base.util.L
import com.inz.z.base.view.AbsBaseFragment

/**
 * CameraX
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/17 09:39.
 */
@Deprecated("Not finish. ")
class CameraXFragment : AbsBaseFragment() {
    companion object {
        private const val TAG = "CameraXFragment"
    }

//    private lateinit var cameraX: CameraX
    /* Internal reference of the [DisplayManager] */
    private lateinit var displayManager: DisplayManager
    private var displayId = -1
//    private var preview: Preview? = null
//    private var previewConfig: PreviewConfig? = null
//    private var imageCapture: ImageCapture? = null
//    private var imageAnalysis: ImageAnalysis? = null

    override fun initWindow() {
        // Mark this as a retain fragment, so the lifecycle does not get restarted on config change
        retainInstance = true
    }

    override fun getLayoutId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        displayManager.unregisterDisplayListener(displayListener)
    }

    /**
     * We need a display listener for orientation changes that do not trigger a configuration
     * change, for example if we choose to override config change in manifest or for 180-degree
     * orientation changes.
     */
    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit
        override fun onDisplayRemoved(displayId: Int) = Unit
        override fun onDisplayChanged(displayId: Int) {
            if (displayId == this@CameraXFragment.displayId) {
                L.i(TAG, "Rotation changed: ${view?.display?.rotation}")
                if (view != null) {
//                    preview?.setTargetRotation(view!!.display.rotation)
//                    imageCapture?.setTargetRotation(view!!.display.rotation)
//                    imageAnalysis?.setTargetRotation(view!!.display.rotation)
                }
            }
        }
    }
}