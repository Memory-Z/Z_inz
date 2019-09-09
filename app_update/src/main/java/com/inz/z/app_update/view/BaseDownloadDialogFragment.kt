package com.inz.z.app_update.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.inz.z.app_update.bean.Constants
import com.inz.z.app_update.http.FileDownloadListener
import com.inz.z.app_update.service.DownloadService
import com.inz.z.app_update.service.DownloadThread
import com.inz.z.app_update.utils.FileUtils
import com.inz.z.app_update.utils.ToastUtils
import com.inz.z.app_update.utils.UpdateShareUtils
import java.io.File

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/14 9:25.
 */
abstract class BaseDownloadDialogFragment : AbsBaseDialogFragment() {

    protected var stopBtn: Button? = null
    protected var backgroundBtn: Button? = null
    protected var downloadProgress: ProgressBar? = null

    protected var mDownloadUrl = ""
    protected var mMustUpdate = false
    protected var mIsShowBackgroundDownload = false
    @DrawableRes
    protected var mNotificationIcon: Int? = null
    protected var mUseOkDownload = true

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
        dialog.setCancelable(false)
        stopBtn = mView!!.findViewById(getStopId())
        backgroundBtn = mView!!.findViewById(getBackgroundId())
        downloadProgress = mView!!.findViewById(getProgressId())
        stopBtn?.setOnClickListener {
            stopTask()
        }
        stopBtn?.visibility = View.INVISIBLE
        backgroundBtn?.setOnClickListener {
            mIsShowBackgroundDownload = true
            backgroundTask()
            downloadHandler?.removeCallbacksAndMessages(progressCallback)
            downloadHandler = null
            UpdateShareUtils.saveIsShowDownload(mContext!!, false)
            this@BaseDownloadDialogFragment.dismiss()
        }
        startTask()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        mDownloadUrl = bundle!!.getString(Constants.DOWNLOAD_URL, "")
        mNotificationIcon = bundle.getInt(Constants.NOTIFICATION_ICON)
        mMustUpdate = bundle.getBoolean(Constants.MUST_UPDATE, false)
        mUseOkDownload = bundle.getBoolean(Constants.USE_OK_DOWNLOAD, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mNotificationIcon == null) {
            mNotificationIcon = mContext!!.applicationInfo.icon
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!mIsShowBackgroundDownload) {
            cancelTask()
        }
        downloadHandler?.removeCallbacksAndMessages(progressCallback)
        downloadHandler = null
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
            downloadService?.useOkDownload = mUseOkDownload
            downloadService?.mFileDownloadListener = FileDownloadListenerImpl()
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
            downloadHandler?.sendMessage(message)
        }

        override fun onError(e: Exception) {
            val bundle = Bundle()
            bundle.putString("error", e.message)
            val message = Message()
            message.what = HANDLER_ERROR
            message.data = bundle
            downloadHandler?.sendMessage(message)
        }
    }

    companion object {
        private const val HANDLER_UPDATE = 0x22000
        private const val HANDLER_ERROR = 0x21000

        private const val HANDLER_START = 0x10001
        private const val HANDLER_AMOUNT = 0x10002
        private const val HANDLER_PROGRESS = 0x10003
        private const val HANDLER_SPEED = 0x10004
        private const val HANDLER_DONE = 0x10005
    }

    private var progressCallback = DownloadHandlerCallback()
    private var downloadHandler: Handler? = Handler(progressCallback)

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
                    val done = bundle.getBoolean("done", false)
                    downloadService?.updateNotification(current.toInt(), count.toInt())
                    if (done) {
                        val file = File(FileUtils.getApkFilePath(mContext!!, mDownloadUrl))
                        val intent = FileUtils.openApkFile(mContext!!, file)
                        mContext!!.startActivity(intent)
                        UpdateShareUtils.saveIsShowDownload(mContext!!, false)
                        this@BaseDownloadDialogFragment.dismiss()
                    }
                    downloadProgress!!.max = count.toInt()
                    downloadProgress!!.progress = current.toInt()

                }
                HANDLER_ERROR -> {
                    val error = bundle!!.getString("error", "")
                    if (mContext != null) {
                        ToastUtils.show(mContext!!, error)
                        UpdateShareUtils.saveIsShowDownload(mContext!!, false)
                        downloadService?.setRunningStatus(false)
                        this@BaseDownloadDialogFragment.dismiss()
                    }
                }

                HANDLER_START -> {

                }
                HANDLER_AMOUNT -> {
                    val contentLength = bundle!!.getLong("contentLength", 100L)
                    downloadProgress?.max = contentLength.toInt()
                }
                HANDLER_SPEED -> {
                    val speed = bundle!!.getString("taskSpeed", "")

                }
                HANDLER_PROGRESS -> {
                    val readBytes = bundle!!.getLong("progress", 0L)
                    val contentLength = bundle.getLong("contentLength", 100L)
                    downloadProgress?.progress = readBytes.toInt()
                    downloadService?.updateNotification(readBytes.toInt(), contentLength.toInt())
                }
                HANDLER_DONE -> {
                    downloadService?.setRunningStatus(false)
                    UpdateShareUtils.saveIsShowDownload(mContext!!, false)
                    this@BaseDownloadDialogFragment.dismiss()
                    val file = File(FileUtils.getApkFilePath(mContext!!, mDownloadUrl))
                    val intent = FileUtils.openApkFile(mContext!!, file)
                    startActivity(intent)
                }
            }
            return false
        }
    }

    private inner class FileDownloadListenerImpl : FileDownloadListener {
        var contentLength = 100L
        override fun start() {
            downloadService?.setRunningStatus(true)
        }

        override fun taskContent(contentLength: Long) {
            val message = Message()
            val bundle = Bundle()
            bundle.putLong("contentLength", contentLength)
            message.what = HANDLER_AMOUNT
            message.data = bundle
            downloadHandler?.sendMessage(message)
            this.contentLength = contentLength
        }

        override fun taskSpeed(speed: String?) {
            val message = Message()
            val bundle = Bundle()
            bundle.putString("taskSpeed", speed)
            message.what = HANDLER_SPEED
            message.data = bundle
            downloadHandler?.sendMessage(message)
        }

        override fun progress(readBytes: Long) {
            val message = Message()
            val bundle = Bundle()
            bundle.putLong("progress", readBytes)
            bundle.putLong("contentLength", contentLength)
            message.what = HANDLER_PROGRESS
            message.data = bundle
            downloadHandler?.sendMessage(message)
        }

        override fun onError(error: String?) {
            val message = Message()
            val bundle = Bundle()
            bundle.putString("error", error)
            message.what = HANDLER_ERROR
            message.data = bundle
            downloadHandler?.sendMessage(message)
        }

        override fun done() {
            val message = Message()
            val bundle = Bundle()
            message.what = HANDLER_DONE
            message.data = bundle
            downloadHandler?.sendMessage(message)
        }
    }
}