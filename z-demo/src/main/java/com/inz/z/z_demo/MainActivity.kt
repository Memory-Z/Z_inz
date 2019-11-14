package com.inz.z.z_demo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.inz.z.base.util.L
import com.inz.z.base.view.AbsBaseActivity
import kotlinx.android.synthetic.main.main_activity.*
import java.net.URL

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/14 14:38.
 */
class MainActivity : AbsBaseActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    override fun initWindow() {

    }

    override fun getLayoutId(): Int {
        return R.layout.main_activity
    }

    override fun initView() {

    }

    override fun initData() {

    }

    val PERMISSIONS =
        arrayOf(
            Manifest.permission.CALL_PHONE
        )

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
        } else {
            packageManager.checkPermission(
                Manifest.permission.CALL_PHONE,
                packageName
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun call(v: View) {
        L.i(TAG, "call --- ")
        if (checkPermission()) {
            val tellPhoneStr = main_tell_et.text.toString()
            val intent = Intent(Intent.ACTION_CALL)
            intent.setData(Uri.parse("tel:$tellPhoneStr"))
            startActivity(intent)
        } else {
            Toast.makeText(mContext, "请授予程序电话权限", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this, PERMISSIONS, 9999)
        }
    }

    fun callDial(v: View) {
        L.i(TAG, "callDial --- ")
        if (checkPermission()) {
            val tellPhoneStr = main_tell_et.text.toString()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:$tellPhoneStr"))
            startActivity(intent)
        } else {
            Toast.makeText(mContext, "请授予程序电话权限", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this, PERMISSIONS, 9999)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 9999) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(mContext, "电话权限已获取", Toast.LENGTH_SHORT).show()
            }
        }
    }
}