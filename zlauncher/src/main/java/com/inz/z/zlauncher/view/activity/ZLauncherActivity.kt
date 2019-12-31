package com.inz.z.zlauncher.view.activity

import android.content.ComponentName
import android.content.Intent
import android.util.LayoutDirection
import android.view.View
import androidx.constraintlayout.solver.widgets.ConstraintHorizontalLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inz.z.base.util.L
import com.inz.z.base.view.AbsBaseActivity
import com.inz.z.zlauncher.R
import com.inz.z.zlauncher.view.activity.adapter.LauncherIconAdapter
import kotlinx.android.synthetic.main.launcher_layout.*
import java.nio.file.DirectoryIteratorException

/**
 * 桌面
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/12/31 10:27.
 */
class ZLauncherActivity : AbsBaseActivity() {
    companion object {
        const val TAG = "ZLauncherActivity"
    }

    private var launcherIconAdapter: LauncherIconAdapter? = null

    override fun initWindow() {

    }

    override fun getLayoutId(): Int {
        return R.layout.launcher_layout
    }

    override fun initView() {
        val layoutManager = GridLayoutManager(mContext, GridLayoutManager.VERTICAL)
        layoutManager.apply {
            spanCount = 4
        }
        launcherIconAdapter = LauncherIconAdapter(mContext)
            .apply {
                setLauncherIconAdapterListener(
                    object : LauncherIconAdapter.LauncherIconAdapterListener {
                        override fun onItemClick(v: View?, position: Int) {
                            val resolveInfo = launcherIconAdapter?.list?.get(position)
                            if (resolveInfo != null) {
                                val componentName =
                                    ComponentName(
                                        resolveInfo.activityInfo.packageName,
                                        resolveInfo.activityInfo.name
                                    )
                                val intent = Intent()
                                    .apply {
                                        component = componentName
                                    }
                                startActivity(intent)
                            }
                        }
                    }
                )
            }


        launcher_rv.apply {
            setLayoutManager(layoutManager)
            adapter = launcherIconAdapter
        }


    }

    override fun initData() {
        loadSystemAllApplication()
    }

    /**
     * 加载系统全部应用程序
     */
    private fun loadSystemAllApplication() {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
            .addCategory(Intent.CATEGORY_LAUNCHER)
        var allApplications = packageManager.queryIntentActivities(mainIntent, 0)
        allApplications = allApplications.filter {
            val packageName = it.activityInfo.packageName
            !getPackageName().equals(packageName)
                    && !packageName.contains("com.pzdf")
                    && !packageName.contains("com.android.pzPhone")
                    && !packageName.contains("com.android.pzMediaPlayer")
                    && !packageName.contains("com.android.deskclock")

        }
        L.i(TAG, " all Applications [ $allApplications ]")
        launcherIconAdapter?.refreshData(allApplications)
    }
}