package com.inz.z.app_update.service

import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 *
 * 下载线程
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/9 10:15.
 */
public class DownloadRunnable(
    var mFilePath: String?,
    var mDownloadUrl: String?,
    var mProgressListener: ProgressListener?
) : Runnable {


    var inputStream: InputStream? = null
    var fileOutPutStream: FileOutputStream? = null


    override fun run() {
        var connection: HttpURLConnection? = null

        try {
            val url = URL(mDownloadUrl)
            if (mDownloadUrl!!.startsWith("https://")) {
                TrustAllCertificates.install()
            }
            connection = url.openConnection() as HttpURLConnection?
            connection!!.setRequestProperty("Connection", "Keep-Alive")
            connection.setRequestProperty("Keep-Alive", "header")

            inputStream = BufferedInputStream(connection.inputStream)
            val count = connection.contentLength
            if (count < 0) {
                return
            }
            if (inputStream == null) {
                return
            }
            writeToFile(count, mFilePath!!, inputStream!!)
        } catch (e: Exception) {
            e.printStackTrace()
            mProgressListener!!.onError(e)
        } finally {

        }
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun writeToFile(count: Int, filePath: String, inputStream: InputStream) {
        val buf = ByteArray(2048)
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
        fileOutPutStream = FileOutputStream(file)
        var bytesRead = 0

        var len: Int = inputStream.read(buf)
        while (len != -1) {
            fileOutPutStream!!.write(buf, 0, len)
            bytesRead += len
            if (mProgressListener != null) {
                val done = bytesRead == count
                mProgressListener?.update(bytesRead.toLong(), count.toLong(), done)
            }
            len = inputStream.read(buf)
        }

    }

    /**
     * 进度监听
     */
    public interface ProgressListener {

        /**
         * 下载进度
         * @param bytesRead 下载进度
         * @param contentLength 总长度
         * @param isDone 是否下载完毕
         */
        fun update(bytesRead: Long, contentLength: Long, isDone: Boolean)

        /**
         * 异常处理
         */
        fun onError(e: Exception)
    }
}