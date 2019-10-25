package com.inz.z.note_book.view.fragment

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.inz.z.base.util.L
import com.inz.z.base.util.LauncherHelper
import com.inz.z.base.view.AbsBaseFragment
import com.inz.z.note_book.R
import kotlinx.android.synthetic.main.note_nav_hint_layout.*
import java.lang.Exception
import java.util.*

/**
 * 首页导航页
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/25 11:48.
 */
class NoteNavFragment : AbsBaseFragment() {

    companion object {
        private const val TAG = "NoteNavFragment"
    }

    override fun initWindow() {
    }

    override fun getLayoutId(): Int {
        return R.layout.note_nav_layout
    }

    override fun initView() {
    }

    override fun initData() {
        val date = Calendar.getInstance(Locale.CHINA).time
        note_nav_hint_year_tv.text =
            String.format(getString(R.string.base_format_year_month), date)
        note_nav_hint_data_tv.text =
            String.format(getString(R.string.base_format_day), date)
        note_nav_hint_week_tv.text =
            String.format(getString(R.string.base_format_week), date)

        note_nav_hint_ibtn.setOnClickListener { imageButton: View? ->
            val packageName = "com.netease.cloudmusic"
            try {
                LauncherHelper.launcherPackageName(mContext, packageName)
            } catch (e: Exception) {
                Toast.makeText(mContext, "启动 $packageName. 失败", Toast.LENGTH_SHORT).show()
            }
        }

    }

}