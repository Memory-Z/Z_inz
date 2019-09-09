package com.inz.z.app_update.view

import android.os.Bundle
import androidx.annotation.IdRes
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.inz.z.app_update.R
import com.inz.z.app_update.bean.Constants
import com.inz.z.app_update.bean.VersionBean
import com.inz.z.app_update.utils.NetUtils
import com.inz.z.app_update.utils.UpdateShareUtils

/**
 * 基础检测更新弹窗
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/14 9:27.
 */
public abstract class BaseUpdateDialogFragment : AbsBaseDialogFragment() {

    /**
     * 是否显示更新
     */
    public var isShowUpdateFragment = false

    protected var mVersionBean: VersionBean? = null
    protected var mToastMsg = ""
    protected var mIsShowToast = false
    protected var mNotificationIcon: Int? = null
    protected var mMustUpdate = false
    protected var mUseOkDownload = true

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
            UpdateShareUtils.saveIsShowUpdate(mContext!!, false)
            update()
        }
        cancelBtn!!.setOnClickListener {
            this.dialog.dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        mVersionBean = bundle!!.getSerializable(Constants.MODEL) as VersionBean?
        mToastMsg = bundle.getString(Constants.TOAST_MSG, "")
        mIsShowToast = bundle.getBoolean(Constants.IS_SHOW_TOAST_MSG, false)
        mNotificationIcon = bundle.getInt(Constants.NOTIFICATION_ICON)
        mMustUpdate = bundle.getBoolean(Constants.MUST_UPDATE, false)
        mUseOkDownload = bundle.getBoolean(Constants.USE_OK_DOWNLOAD, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mNotificationIcon == null) {
            mNotificationIcon = mContext!!.applicationInfo.icon
        }
        isShowUpdateFragment = true;
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
                mMustUpdate,
                mUseOkDownload
            )
        }
        UpdateShareUtils.saveIsShowDownload(mContext!!, true)
        downloadDialogFragment?.show(
            activity!!.supportFragmentManager,
            "BaseDownloadDialogFragment"
        )
        UpdateShareUtils.saveIsShowUpdate(mContext!!, false)
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