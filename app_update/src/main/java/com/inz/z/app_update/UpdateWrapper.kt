package com.inz.z.app_update

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.annotation.DrawableRes
import androidx.fragment.app.FragmentActivity
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import com.inz.z.app_update.bean.VersionBean
import com.inz.z.app_update.service.CheckUpdateThread
import com.inz.z.app_update.service.DownloadService
import com.inz.z.app_update.utils.FileUtils
import com.inz.z.app_update.utils.NetUtils
import com.inz.z.app_update.utils.ToastUtils
import com.inz.z.app_update.utils.UpdateShareUtils
import com.inz.z.app_update.view.AppUpdateDialogFragment
import com.inz.z.app_update.view.BaseDownloadDialogFragment
import com.inz.z.app_update.view.BaseUpdateDialogFragment
import java.io.File

/**
 *
 * 更新包装
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/13 11:39.
 */
class UpdateWrapper() {

    companion object {
        /**
         * 是否检测 过更新
         */
        var isCheckedUpdate = false
    }

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
    var mCls: Class<in androidx.fragment.app.FragmentActivity>? = null
    var mHandler = Handler(Looper.getMainLooper())
    private var mDownloadFragment: BaseDownloadDialogFragment? = null


    fun start() {
        val updateTime = UpdateShareUtils.getUpdateTime(mContext!!)
        val currentTime = System.currentTimeMillis()
        if (isCheckedUpdate || currentTime - updateTime < mTime) {
            return
        }
        isCheckedUpdate = true
        UpdateShareUtils.saveUpdateTime(mContext!!, currentTime)
        if (!NetUtils.getNetworkStatus(mContext!!)) {
            if (mIsShowNetworkErrorToast) {
                ToastUtils.show(mContext!!, R.string.app_update_lib_default_toast)
            }
            return
        }
        if (TextUtils.isEmpty(mUrl)) {
//            throw RuntimeException("url not be null")
            Log.e("UpdateWrapper", "url is null")
            return
        } else if (!checkUpdateStatus()) {
            if (checkUpdateThread == null) {
                checkUpdateThread = CheckUpdateThread(
                    mUrl,
                    mContext!!,
                    mIsPost,
                    mPostParams,
                    cUpdateCallback
                )
            }
            checkUpdateThread?.start()
        }
    }

    private val cUpdateCallback = CheckUpdateCallbackImpl()
    private var checkUpdateThread: CheckUpdateThread? = null

    /**
     * 获取更新状态
     */
    private fun checkUpdateStatus(): Boolean {
        var flag = false
        val isShowUpdate = UpdateShareUtils.getIsShowUpdate(mContext!!)
        val isShowDownload = UpdateShareUtils.getIsShowDownload(mContext!!)
        val isDownload = DownloadService.isRunning
        if (isShowUpdate || isShowDownload || isDownload) {
            // 如果当前状态为 显示更新框、 显示下载框、正在下载
            flag = true
        }
        return flag
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

        fun setCustomsActivity(cls: Class<in androidx.fragment.app.FragmentActivity>): Builder {
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

    private var updateDialogFragment: BaseUpdateDialogFragment? = null

    /**
     * 显示更新提示
     */
    fun showUpdateDialog(activity: AppCompatActivity, versionBean: VersionBean) {
        if (updateDialogFragment == null) {
            updateDialogFragment =
                AppUpdateDialogFragment.newInstance(
                    versionBean,
                    notificationIcon!!,
                    mToastMsg,
                    mIsShowToast,
                    false,
                    true
                )
        }
        if (mDownloadFragment != null) {
            updateDialogFragment?.setDownloadFragment(mDownloadFragment!!)
        }
        if (!updateDialogFragment!!.isShowUpdateFragment) {
            UpdateShareUtils.saveIsShowUpdate(mContext!!, true)
            updateDialogFragment?.show(activity.supportFragmentManager, "BaseUpdateDialogFragment")
        }
    }


    /**
     * 检测更新回调
     */
    inner class CheckUpdateCallbackImpl : CheckUpdateThread.CallBack {
        override fun callBack(versionBean: VersionBean?, hasNewVersion: Boolean) {
            val newVersionCode = versionBean?.versionCode
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
                val apkIsDownload = UpdateShareUtils.getDownloadApk(mContext!!)
                val downloadVersionCode = UpdateShareUtils.getDownloadApkVersion(mContext!!)

                if (apkIsDownload && newVersionCode == downloadVersionCode) {
                    val file = File(FileUtils.getApkFilePath(mContext!!, mUrl))
                    val intent = Intent(FileUtils.openApkFile(mContext!!, file))
                    mContext?.startActivity(intent)
                    return
                }
//                startToActivity(mContext!!, versionBean)
                UpdateShareUtils.saveDownloadApkVersion(mContext!!, newVersionCode!!)
                showUpdateDialog(mActivity!!, versionBean)
            }
        }
    }
}