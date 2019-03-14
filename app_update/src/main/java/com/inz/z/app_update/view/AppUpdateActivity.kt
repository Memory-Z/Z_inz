package com.inz.z.app_update.view

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.inz.z.app_update.R

/**
 * 更新界面
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/13 14:07.
 */
class AppUpdateActivity : AbsBaseActivity() {
    override fun getUpdateDialogFragment(): Fragment {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDownloadDialogFragment(): Fragment {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_update
    }
}