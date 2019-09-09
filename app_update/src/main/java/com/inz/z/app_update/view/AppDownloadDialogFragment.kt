package com.inz.z.app_update.view

import android.os.Bundle
import androidx.annotation.DrawableRes
import com.inz.z.app_update.R
import com.inz.z.app_update.bean.Constants

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/14 10:02.
 */
class AppDownloadDialogFragment : BaseDownloadDialogFragment() {


    override fun getStopId(): Int {
        return R.id.app_update_fragment_stop_download_btn
    }

    override fun getBackgroundId(): Int {
        return R.id.app_update_fragment_background_download_btn
    }

    override fun getProgressId(): Int {
        return R.id.app_update_fragment_download_pb
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_download
    }

    override fun initData() {

    }

    companion object {
        fun newInstance(
            downloadUrl: String,
            @DrawableRes notificationIcon: Int,
            mustUpdate: Boolean,
            userOkDownload: Boolean
        ): BaseDownloadDialogFragment {
            val bundle = Bundle()
            bundle.putString(Constants.DOWNLOAD_URL, downloadUrl)
            bundle.putInt(Constants.NOTIFICATION_ICON, notificationIcon)
            bundle.putBoolean(Constants.MUST_UPDATE, mustUpdate)
            bundle.putBoolean(Constants.USE_OK_DOWNLOAD, userOkDownload)
            val downloadDialogFragment = AppDownloadDialogFragment()
            downloadDialogFragment.arguments = bundle
            return downloadDialogFragment
        }
    }

}