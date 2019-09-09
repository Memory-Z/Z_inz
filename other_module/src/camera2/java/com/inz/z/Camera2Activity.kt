package com.inz.z

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.inz.z.other_module.R
import kotlinx.android.synthetic.camera2.camera2_activity.*
import kotlinx.android.synthetic.camera2.camera2_activity.view.*
import kotlinx.android.synthetic.camera2.camera2_activity.view.camers2_fl

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/29 14:27.
 */
@Route(path = "/other/camera2")
class Camera2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera2_activity)
        savedInstanceState ?: supportFragmentManager.beginTransaction()
            .replace(camers2_fl.id, Camera2FragmentT2.newInstance())
            .commit()
    }
}