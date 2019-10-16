package com.inz.z.base_camera.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.cameraview.R
import com.inz.z.base_camera.view.fragment.BaseCameraFragment

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/09/10 09:43.
 */
@Route(path = "/camera/base_camera", group = "camera")
class BaseCameraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)
        supportFragmentManager.beginTransaction()
            .replace(R.id.base_activity_fl, BaseCameraFragment.newInstance(), "BaseCameraFragment")
            .commit()
    }
}