package com.inz.z.finger

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal
import com.alibaba.android.arouter.facade.annotation.Route
import com.inz.z.base.util.L
import com.inz.z.other_module.R
import java.util.concurrent.Executors

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/11 14:13.
 */
@Route(path = "/other/finger", group = "other")
class FingerBaseActivity : AppCompatActivity() {

    companion object {
        const val TAG = "FingerBaseActivity"
        const val REQUEST_PERMISSION_CODE = 0x989

    }

    private val permissionGroups: Array<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            arrayOf(
                Manifest.permission.USE_BIOMETRIC,
                Manifest.permission.USE_FINGERPRINT
            )
        } else {
            emptyArray()
        }

    private var mContext: Context? = null
    private var fingerHandler = Handler(FingerHandlerCallback())
    private var executorService = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.finger_base_layout)
        mContext = this
        initView()
    }

    fun initView() {
        val havePermission = checkPermission()
        if (havePermission) {
            // 开始识别
            authBiometric()
        } else {
            // 请求权限
            requestPermission()
        }
    }

    /**
     * 检测权限
     */
    private fun checkPermission(): Boolean {
        var havePermission = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            havePermission =
                checkSelfPermission(Manifest.permission.USE_BIOMETRIC) == PackageManager.PERMISSION_GRANTED
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            havePermission =
                checkSelfPermission(Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED
        }
        return havePermission
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionGroups, REQUEST_PERMISSION_CODE)
        } else {
            Toast.makeText(mContext, "手机硬件不支持 ！！！！ ", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 验证生物识别
     */
    private fun authBiometric() {
        L.i(TAG, "------ authBiometric")
        if (mContext == null) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val biometric = BiometricPrompt.Builder(mContext)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                biometric.setConfirmationRequired(true)
                    .setDeviceCredentialAllowed(true)
            }
            val prompt = biometric.setDescription("启用指纹识别")
//                .setSubtitle("SubTitle")
                .setNegativeButton("关闭", executorService,
                    DialogInterface.OnClickListener { dialog, which ->
                        L.i(TAG, "-----------------------click close . ")
                        runOnUiThread {
                            Toast.makeText(
                                mContext,
                                "点击 了 关闭 按钮",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                .setTitle("必须要有的标题")
                .build()
            val cancel: android.os.CancellationSignal = android.os.CancellationSignal()
                .apply {
                    setOnCancelListener {
                        L.i(TAG, "setOnCancelListener ")
                    }
                }

            prompt.authenticate(
                cancel,
                executorService,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(
                        errMsgId: Int,
                        errString: CharSequence?
                    ) {
                        super.onAuthenticationError(errMsgId, errString)
                        L.i(TAG, "--------------onAuthenticationError: ${errString}")
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                        super.onAuthenticationSucceeded(result)
                        L.i(TAG, "----------------onAuthenticationSucceeded")
                    }

                    override fun onAuthenticationHelp(
                        helpMsgId: Int,
                        helpString: CharSequence?
                    ) {
                        super.onAuthenticationHelp(helpMsgId, helpString)
                        L.i(TAG, "--------------onAuthenticationHelp")
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        L.i(TAG, "--------------onAuthenticationFailed")
                    }
                })

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val manager = FingerprintManagerCompat.from(mContext!!)
            if (manager.isHardwareDetected && manager.hasEnrolledFingerprints()) {
                manager.authenticate(
                    null,
                    0,
                    CancellationSignal(),
                    object : FingerprintManagerCompat.AuthenticationCallback() {
                        override fun onAuthenticationError(
                            errMsgId: Int,
                            errString: CharSequence?
                        ) {
                            super.onAuthenticationError(errMsgId, errString)
                            L.i(TAG, "--------------onAuthenticationError")
                        }

                        override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
                            super.onAuthenticationSucceeded(result)
                            L.i(TAG, "--------------onAuthenticationSucceeded")
                        }

                        override fun onAuthenticationHelp(
                            helpMsgId: Int,
                            helpString: CharSequence?
                        ) {
                            super.onAuthenticationHelp(helpMsgId, helpString)
                            L.i(TAG, "--------------onAuthenticationHelp")
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            L.i(TAG, "--------------onAuthenticationFailed")
                        }
                    },
                    fingerHandler
                )
            }
        } else {
            Toast.makeText(mContext, "手机系统 低于 5.1 不支持指纹识别 ！！！ ", Toast.LENGTH_LONG).show()
        }
    }

    private inner class FingerHandlerCallback : Handler.Callback {
        override fun handleMessage(msg: Message?): Boolean {
            return true
        }
    }


}