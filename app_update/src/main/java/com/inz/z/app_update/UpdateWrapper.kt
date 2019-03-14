package com.inz.z.app_update

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.annotation.DrawableRes
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import com.inz.z.app_update.bean.VersionBean
import com.inz.z.app_update.service.CheckUpdateThread
import com.inz.z.app_update.utils.NetUtils
import com.inz.z.app_update.utils.ToastUtils
import com.inz.z.app_update.view.AppUpdateDialogFragment
import com.inz.z.app_update.view.BaseDownloadDialogFragment

/**
 *
 * 更新包装
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/13 11:39.
 */
class UpdateWrapper {

    var mContext: Context? = null
    var mActivity: AppCompatActivity? = null
    var mUrl: String = ""
    var mToastMsg: String = ""
    var checkUpdateCallback: CheckUpdateThread.CallBack? = null
    var notificationIcon: Int? = null
    var mTime: Long = 0L
    var mIsShowToast = true
    var mIsShowNetworkErrorToast = true
    var mIsShowBackgroundDownload = true
    var mIsPost = false
    var mPostParams: Map<String, String>? = null
    var mCls: Class<in FragmentActivity>? = null
    var mHandler = Handler(Looper.getMainLooper())
    private var mDownloadFragment: BaseDownloadDialogFragment? = null


    fun start() {
        if (!NetUtils.getNetworkStatus(mContext!!)) {
            if (mIsShowNetworkErrorToast) {
                ToastUtils.show(mContext!!, R.string.app_update_lib_default_toast)
            }
            return
        }
        if (TextUtils.isEmpty(mUrl)) {
            throw RuntimeException("url not be null")
        }
        CheckUpdateThread(mUrl, mContext!!, mIsPost, mPostParams, CheckUpdateCallbackImpl()).start()
    }

    /**
     * 构造器
     */
    public class Builder(activity: AppCompatActivity) {
        private val wrapper = UpdateWrapper()

        init {
            wrapper.mActivity = activity
            wrapper.mContext = activity
        }

        fun setUrl(url: String): Builder {
            wrapper.mUrl = url
            return this
        }

        fun setTime(time: Long): Builder {
            wrapper.mTime = time
            return this
        }

        fun setNotificationIcon(@DrawableRes notificationIcon: Int): Builder {
            wrapper.notificationIcon = notificationIcon
            return this
        }

        fun setCustomsActivity(cls: Class<in FragmentActivity>): Builder {
            wrapper.mCls = cls
            return this
        }

        fun setDownloadFragment(downloadFragment: BaseDownloadDialogFragment): Builder {
            wrapper.mDownloadFragment = downloadFragment
            return this
        }

        fun setCallback(callback: CheckUpdateThread.CallBack): Builder {
            wrapper.checkUpdateCallback = callback
            return this
        }

        fun setToastMsg(toastMsg: String): Builder {
            wrapper.mToastMsg = toastMsg
            return this
        }

        fun setIsShowToast(isShowToast: Boolean): Builder {
            wrapper.mIsShowToast = isShowToast
            return this
        }

        fun setIsShowBackgroundDownload(isSHowBackgroundDownload: Boolean): Builder {
            wrapper.mIsShowBackgroundDownload = isSHowBackgroundDownload
            return this
        }

        fun setIsPost(isPost: Boolean): Builder {
            wrapper.mIsPost = isPost
            return this
        }

        fun setPostParams(postPrams: Map<String, String>): Builder {
            wrapper.mPostParams = postPrams
            return this
        }

        fun build(): UpdateWrapper {
            return wrapper
        }
    }

    /**
     * 显示更新提示
     */
    fun showUpdateDialog(activity: AppCompatActivity, versionBean: VersionBean) {
        val updateDialogFragment =
            AppUpdateDialogFragment.newInstance(
                versionBean,
                notificationIcon!!,
                mToastMsg,
                mIsShowToast,
                false
            )
        if (mDownloadFragment != null) {
            updateDialogFragment.setDownloadFragment(mDownloadFragment!!)
        }
        updateDialogFragment.show(activity.supportFragmentManager, "BaseUpdateDialogFragment")
    }


    /**
     * 检测更新回调
     */
    inner class CheckUpdateCallbackImpl : CheckUpdateThread.CallBack {
        override fun callBack(versionBean: VersionBean?, hasNewVersion: Boolean) {
            if (versionBean == null) {
                mHandler.post(Runnable {
                    kotlin.run {
                        if (mIsShowToast) {
                            ToastUtils.show(
                                mContext!!,
                                if (TextUtils.isEmpty(mToastMsg))
                                    mContext!!.resources.getString(R.string.app_update_lib_default_toast)
                                else
                                    mToastMsg
                            )
                        }
                    }
                })
                return
            }
            if (checkUpdateCallback != null) {
                checkUpdateCallback!!.callBack(versionBean, hasNewVersion)
            }
            if (hasNewVersion || mIsShowToast) {
//                startToActivity(mContext!!, versionBean)
                showUpdateDialog(mActivity!!, versionBean)
            }
        }
    }
}