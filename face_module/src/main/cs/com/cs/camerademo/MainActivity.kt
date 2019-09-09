package com.cs.camerademo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.cs.camerademo.camera1.CameraActivity
import com.cs.camerademo.camera2.CameraActivity2
import com.cs.camerademo.camera2.CameraActivity2Face
import com.inz.z.face_module.R
import kotlinx.android.synthetic.main.activity_main.*

@Route(path = "/face/csCamera", group = "face")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btCapture.setOnClickListener { startActivity(Intent(this, CaptureActivity::class.java)) }
        btCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra(CameraActivity.TYPE_TAG, CameraActivity.TYPE_CAPTURE)
            startActivity(intent)
        }
        btCameraRecord.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra(CameraActivity.TYPE_TAG, CameraActivity.TYPE_RECORD)
            startActivity(intent)
        }

        btCamera2.setOnClickListener {
            val intent = Intent(this, CameraActivity2::class.java)
            startActivity(intent)
        }

        btCamera2Face.setOnClickListener {
            startActivity(Intent(this, CameraActivity2Face::class.java))
        }
    }
}

