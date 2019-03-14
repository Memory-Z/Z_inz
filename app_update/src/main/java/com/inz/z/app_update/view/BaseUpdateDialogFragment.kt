package com.inz.z.app_update.view

import android.os.Bundle
import android.support.annotation.IdRes
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.inz.z.app_update.R
import com.inz.z.app_update.bean.VersionBean
import com.inz.z.app_update.utils.NetUtils
import java.lang.StringBuilder

/**
 * 基础检测更新弹窗
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/14 9:27.
 */
abstract class BaseUpdateDialogFragment : AbsBaseDialogFragment() {

    protected var mVersionBean: VersionBean? = null
    protected var mToastMsg = ""
    protected var mIsShowToast = false
    protected var mNotificationIcon: Int? = null
    protected var mMustUpdate = false

    protected var updateBtn: Button? = null
    protected var cancelBtn: Button? = null
    protected var contentTv: TextView? = null
    protected var downloadDialogFragment: BaseDownloadDialogFragment? = null

    /**
     * 更新
     */
    @IdRes
    protected abstract fun getUpdateId(): Int

    /**
     * 取消
     */
    @IdRes
    protected abstract fun getCancelId(): Int

    /**
     * 内容
     */
    @IdRes
    protected abstract fun getContentId(): Int

    override fun initView() {
        updateBtn = mView!!.findViewById(getUpdateId())
        cancelBtn = mView!!.findViewById(getCancelId())
        contentTv = mView!!.findViewById(getContentId())
        contentTv?.text = getUpdateContent()
        updateBtn!!.setOnClickListener {
            update()
        }
        cancelBtn!!.setOnClickListener {
            this.dialog.dismiss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mNotificationIcon == null) {
            mNotificationIcon = mContext!!.applicationInfo.icon
        }
    }

    /**
     * 开始更新
     */
    private fun update() {
        if (!NetUtils.getNetworkStatus(mContext!!)) {
            return
        }
        if (downloadDialogFragment == null) {
            downloadDialogFragment = AppDownloadDialogFragment.newInstance(
                mVersionBean!!.url,
                mNotificationIcon!!,
                mMustUpdate
            )
        }
        downloadDialogFragment?.show(
            activity!!.supportFragmentManager,
            "BaseDownloadDialogFragment"
        )
        this.dismiss()
    }

    public fun setDownloadFragment(baseDownloadDialogFragment: BaseDownloadDialogFragment) {
        this.downloadDialogFragment = baseDownloadDialogFragment
    }

    /**
     * 获取更新内容
     */
    private fun getUpdateContent(): String {
        val sb = StringBuilder()
        sb.append(mContext!!.resources.getString(R.string.update_lib_version_code))
            .append(mVersionBean!!.versionName)
            .append("\n")
            .append("\n")
            .append(mContext!!.resources.getString(R.string.update_lib_update_content))
            .append("\n")
            .append(mVersionBean!!.content.replace("#", "\\\n"))
        return sb.toString()
    }

}