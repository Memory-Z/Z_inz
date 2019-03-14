package com.inz.z.app_update.view

import android.os.Bundle
import android.support.annotation.DrawableRes
import com.inz.z.app_update.R
import com.inz.z.app_update.bean.Constants
import com.inz.z.app_update.bean.VersionBean

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/13 18:38.
 */
class AppUpdateDialogFragment : BaseUpdateDialogFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_update
    }

    override fun initData() {

    }

    override fun getUpdateId(): Int {
        return R.id.app_update_fragment_update_btn
    }

    override fun getCancelId(): Int {
        return R.id.app_update_fragment_cancel_btn
    }

    override fun getContentId(): Int {
        return R.id.app_update_fragment_content_tv
    }

    companion object {
        fun newInstance(
            versionBean: VersionBean,
            @DrawableRes notificationIcon: Int,
            toastMsg: String,
            isShowToast: Boolean,
            isMustUpdate: Boolean
        ): AppUpdateDialogFragment {
            val bundle = Bundle()
            bundle.putSerializable(Constants.MODEL, versionBean)
            bundle.putString(Constants.TOAST_MSG, toastMsg)
            bundle.putBoolean(Constants.IS_SHOW_TOAST_MSG, isShowToast)
            bundle.putInt(Constants.NOTIFICATION_ICON, notificationIcon)
            bundle.putBoolean(Constants.MUST_UPDATE, isMustUpdate)
            val appUpdateDialogFragment = AppUpdateDialogFragment()
            appUpdateDialogFragment.arguments = bundle
            return appUpdateDialogFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        mVersionBean = bundle!!.getSerializable(Constants.MODEL) as VersionBean?
        mToastMsg = bundle.getString(Constants.TOAST_MSG, "")
        mIsShowToast = bundle.getBoolean(Constants.IS_SHOW_TOAST_MSG, false)
        mNotificationIcon =
            bundle.getInt(Constants.NOTIFICATION_ICON, mContext!!.applicationInfo.icon)
        mMustUpdate = bundle.getBoolean(Constants.MUST_UPDATE, false)
    }
}