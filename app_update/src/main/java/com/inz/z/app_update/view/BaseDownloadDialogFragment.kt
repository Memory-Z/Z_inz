package com.inz.z.app_update.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.inz.z.app_update.service.DownloadService
import com.inz.z.app_update.service.DownloadThread
import com.inz.z.app_update.utils.ToastUtils

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/14 9:25.
 */
abstract class BaseDownloadDialogFragment : AbsBaseDialogFragment(), View.OnClickListener {

    protected var stopBtn: Button? = null
    protected var backgroundBtn: Button? = null
    protected var downloadProcess: ProgressBar? = null

    protected var mDownloadUrl = ""
    protected var mMustUpdate = false
    protected var mIsShowBackgroundDownload = false
    @DrawableRes
    protected var mNotificationIcon: Int? = null

    /**
     * 暂停
     */
    @IdRes
    protected abstract fun getStopId(): Int

    /**
     * 后台
     */
    @IdRes
    protected abstract fun getBackgroundId(): Int

    @IdRes
    protected abstract fun getProgressId(): Int

    override fun initView() {
        stopBtn = mView!!.findViewById(getStopId())
        backgroundBtn = mView!!.findViewById(getBackgroundId())
        downloadProcess = mView!!.findViewById(getProgressId())
        stopBtn?.setOnClickListener(this)
        backgroundBtn?.setOnClickListener(this)
        startTask()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            getStopId() -> stopTask()
            getBackgroundId() -> backgroundTask()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelTask()
    }

    /**
     * 开始下载
     */
    private fun startTask() {
        val intent = Intent(mContext!!, DownloadService::class.java)
        val bundle = Bundle()
        intent.putExtras(bundle)
        mContext!!.bindService(intent, downloadServiceConnection, Context.BIND_AUTO_CREATE)
    }

    /**
     * 暂停下载
     */
    private fun stopTask() {

    }

    /**
     * 后台下载
     */
    private fun backgroundTask() {
        if (downloadService != null) {
            downloadService?.isBackground = mIsShowBackgroundDownload
        }
    }

    /**
     * 取消下载
     */
    private fun cancelTask() {
        if (downloadService != null) {
            downloadService!!.cancelDownload()
            try {
                mContext!!.unbindService(downloadServiceConnection)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val downloadServiceConnection = DownloadServiceConnection()
    private var downloadService: DownloadService? = null
    private var downloadServiceBinder: DownloadService.DownloadBinder? = null

    /**
     * 下载Service 链接
     */
    private inner class DownloadServiceConnection : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
            downloadService = null
            downloadServiceBinder = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            downloadServiceBinder = service as DownloadService.DownloadBinder?
            downloadService = downloadServiceBinder?.getService()
            downloadService?.mProgressListener = DownloadTaskListenerImpl()
            downloadService?.notificationIcon = mNotificationIcon!!
            downloadService?.startDownload(mDownloadUrl)
        }
    }

    private inner class DownloadTaskListenerImpl : DownloadThread.ProgressListener {
        override fun update(bytesRead: Long, contentLength: Long, isDone: Boolean) {
            val bundle = Bundle()
            bundle.putLong("current", bytesRead)
            bundle.putLong("count", contentLength)
            bundle.putBoolean("done", isDone)
            val message = Message()
            message.what = HANDLER_UPDATE
            message.data = bundle
            downloadHandler.sendMessage(message)
        }

        override fun onError(e: Exception) {
            val bundle = Bundle()
            bundle.putString("error", e.message)
            val message = Message()
            message.what = HANDLER_ERROR
            message.data = bundle
            downloadHandler.sendMessage(message)
        }
    }

    companion object {
        private const val HANDLER_UPDATE = 0x22000
        private const val HANDLER_ERROR = 0x21000
    }

    private var downloadHandler = Handler(DownloadHandlerCallback())

    /**
     * Handler 处理
     */
    private inner class DownloadHandlerCallback : Handler.Callback {
        override fun handleMessage(msg: Message?): Boolean {
            val bundle = msg?.data
            when (msg?.what) {
                HANDLER_UPDATE -> {
                    val current = bundle!!.getLong("current", 0L)
                    val count = bundle.getLong("count", 100)
//                    val done = bundle.getBoolean("done", false)
                    if (downloadService != null) {
                        downloadService?.updateNotification(current.toInt(), count.toInt())
                    }
                }
                HANDLER_ERROR -> {
                    val error = bundle!!.getString("error", "")
                    ToastUtils.show(mContext!!, error)
                }
            }
            return false
        }
    }
}