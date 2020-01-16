package com.inz.z.note_book.view.activity

import android.content.Intent
import android.database.DataSetObservable
import android.database.DataSetObserver
import android.os.Build
import androidx.databinding.Bindable
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import com.inz.z.base.util.L
import com.inz.z.base.view.AbsBaseActivity
import com.inz.z.note_book.BuildConfig
import com.inz.z.note_book.R
import com.inz.z.note_book.databinding.SplashLayoutBinding
import com.inz.z.note_book.service.NotificationForegroundService
import kotlinx.android.synthetic.main.splash_layout.*

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/24 10:05.
 */
class SplashActivity : AbsBaseActivity() {

    companion object {
        const val TAG = "SplashActivity"
    }

    private lateinit var splashLayoutBinding: SplashLayoutBinding
    private var isPause = false


    override fun initWindow() {
    }

    override fun getLayoutId(): Int {
        return R.layout.splash_layout
    }

    override fun useDataBinding(): Boolean {
        return true
    }

    override fun setDataBindingView() {
        super.setDataBindingView()
        splashLayoutBinding =
            DataBindingUtil.setContentView(this@SplashActivity, R.layout.splash_layout)
    }

    override fun initView() {
        splashLayoutBinding.version = BuildConfig.VERSION_NAME
        splashLayoutBinding.timeNumber = time.toString()
    }

    override fun initData() {
        setRightTopTimer()
        val service = Intent(mContext, NotificationForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(service)
        } else {
            startService(service)
        }
    }

    override fun onResume() {
        super.onResume()
        isPause = false
    }

    override fun onPause() {
        super.onPause()
        isPause = true
    }

    private var time = 5

    /**
     * 设置右上角倒计时。
     */
    private fun setRightTopTimer() {
        splash_top_end_num_tv.postDelayed({
            if (time == 0) {
                gotoMainActivity()
            } else {
                time -= 1
                splashLayoutBinding.timeNumber = time.toString()
                setRightTopTimer()
            }
        }, 1000)
    }

    /**
     * 前往主界面
     */
    private fun gotoMainActivity() {

        if (this.isPause) {
            L.i(TAG, "gotoMainActivity: this activity is isPause. ")
            this@SplashActivity.finish()
            return
        }
        val intent = Intent(mContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}