package com.inz.z.face_module.view.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.FaceDetector
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.inz.z.face_module.R
import com.inz.z.face_module.view.widget.FaceImageView
import kotlinx.android.synthetic.main.activity_face_normal.*

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/05/23 17:32.
 */
@Route(path = "/face/one", name = "测试1 ")
public class FaceOneActivity : AppCompatActivity() {

    companion object {
        public const val TAG = "FaceOneActivity"
    }

    var fIv: FaceImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_normal)
        fIv = face_fiv


        val b = BitmapFactory.decodeResource(resources, R.drawable.face_0)
        val faceBitmap = b.copy(Bitmap.Config.RGB_565, true)
        b.recycle()
        val w = faceBitmap.width
        val h = faceBitmap.height
        fIv?.setImageBitmap(faceBitmap)
        val faceDetector = FaceDetector(w, h , 1)
        var faces = arrayOfNulls<FaceDetector.Face>(1)
        val count = faceDetector.findFaces(faceBitmap, faces)
        Log.i(TAG, "count - $count")
        fIv?.setFaces(faces, faceBitmap)

    }
}