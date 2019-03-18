package com.inz.z.app_update.service

import java.io.*
import java.net.HttpURLConnection
import java.net.URL

/**
 *
 * 下载线程
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/9 10:15.
 */
public class DownloadThread(
    private var mFilePath: String?,
    var mDownloadUrl: String?,
    var mProgressListener: ProgressListener?
) : Thread() {


    var inputStream: InputStream? = null
    var fileOutputStream: FileOutputStream? = null


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
            try {
                fileOutputStream!!.flush()
                fileOutputStream!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                inputStream!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            connection?.disconnect()
        }
    }

    private fun writeToFile(count: Int, filePath: String, inputStream: InputStream) {
        val buf = ByteArray(2048)
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
        fileOutputStream = FileOutputStream(file)
        var bytesRead = 0

        var len: Int = inputStream.read(buf)
        while (len != -1) {
            fileOutputStream!!.write(buf, 0, len)
            bytesRead += len
            if (mProgressListener != null) {
                val done = bytesRead >= count
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