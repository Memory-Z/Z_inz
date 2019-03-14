package com.inz.z.app_update.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.inz.z.app_update.bean.Constants
import com.inz.z.app_update.utils.FileUtils
import com.inz.z.app_update.utils.NotificationUtils
import java.io.File

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/13 9:00.
 */
public class DownloadService : Service() {

    private var mContext: Context? = null
    /**
     * 文件保存地址
     */
    var filePath = ""
    /**
     * 是否后台下载
     */
    var isBackground: Boolean = false
    /**
     * 提示图标
     */
    var notificationIcon = applicationInfo.icon
    /**
     * 进度监听
     */
    var mProgressListener: DownloadThread.ProgressListener? = null
    var downloadThread: DownloadThread? = null


    private val downloadBinder = DownloadBinder(this@DownloadService)

    public class DownloadBinder(private val service: DownloadService) : Binder() {
        public fun getService(): DownloadService {
            return service
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return downloadBinder
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        stopSelf()
        return super.onUnbind(intent)
    }

    /**
     * 开始下载
     */
    public fun startDownload(downloadUrl: String) {
        filePath = FileUtils.getApkFilePath(mContext!!, downloadUrl)
        downloadThread = DownloadThread(filePath, downloadUrl, DownloadListenerImpl())
        downloadThread!!.start()
    }

    /**
     * 取消下载
     */
    public fun cancelDownload() {
        if (downloadThread != null) {
            downloadThread!!.interrupt()
            downloadThread = null
        }
    }

    var notificationManager: NotificationManager? = null
    var mBuilder: NotificationCompat.Builder? = null
    val updateNotificationTag = "App_Update_Tag"

    /**
     * 更新通知栏
     * @param current 当前进度
     * @param count 总进度
     */
    public fun updateNotification(current: Int, count: Int) {
        if (mBuilder == null) {
            showNotification(current, count)
            return
        }
        mBuilder!!.setProgress(count, current, false)
        notificationManager!!.notify(
            updateNotificationTag,
            Constants.downloadServiceNotificationId,
            mBuilder!!.build()
        )
    }

    /**
     * 显示通知栏
     * @param current 当前进度
     * @param count 总进度
     */
    private fun showNotification(current: Int, count: Int) {
        val haveNotification = NotificationUtils.isNotificationEnabled(mContext)
        if (haveNotification) {
            notificationManager = NotificationUtils.getNotificationManager(mContext)
            mBuilder =
                NotificationCompat.Builder(mContext!!, Constants.downloadServiceNotificationChannel)
            mBuilder!!.setSmallIcon(notificationIcon)
            mBuilder!!.setChannelId(Constants.downloadServiceNotificationChannel)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    Constants.downloadServiceNotificationChannel,
                    "appUpdateChannel",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager!!.createNotificationChannel(channel)
            }
            mBuilder!!.setTicker("应用更新")
            mBuilder!!.setContentTitle("应用更新")
            mBuilder!!.setProgress(count, current, false)
            notificationManager!!.notify(
                updateNotificationTag,
                Constants.downloadServiceNotificationId,
                mBuilder!!.build()
            )
        }
    }

    /**
     * 取消通知
     */
    private fun cancelNotification() {
        if (notificationManager != null) {
            notificationManager!!.cancel(
                updateNotificationTag,
                Constants.downloadServiceNotificationId
            )
        }
    }

    /**
     * 下载监听实现
     */
    private inner class DownloadListenerImpl : DownloadThread.ProgressListener {
        override fun update(bytesRead: Long, contentLength: Long, isDone: Boolean) {
            if (isDone) {
                // 下载完毕
                if (notificationManager != null) {
                    cancelNotification()
                }
                if (isBackground) {
                    startActivity(FileUtils.openApkFile(mContext!!, File(filePath)))
                }
            } else {
                if (isBackground) {
                    updateNotification(bytesRead.toInt(), contentLength.toInt())
                }
            }
            if (mProgressListener != null) {
                mProgressListener!!.update(bytesRead, contentLength, isDone)
            }
        }

        override fun onError(e: Exception) {
            if (notificationManager != null) {
                cancelNotification()
            }
            if (mProgressListener != null) {
                mProgressListener!!.onError(e)
            }
        }
    }
}