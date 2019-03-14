package com.inz.z.app_update.service

import android.content.Context
import com.inz.z.app_update.bean.VersionBean
import com.inz.z.app_update.utils.PackageUtils
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

/**
 *
 * 检测更新线程
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/9 10:17.
 */
public class CheckUpdateThread(
    /**
     * 检测更新地址
     */
    private var checkUpdateUrl: String,
    /**
     * 上下文
     */
    private var mContext: Context?,
    /**
     * 是否未POST 请求
     */
    private var isPost: Boolean,
    /**
     * Post 请求参数
     */
    private var postParams: Map<String, String>?,
    /**
     * 回调函数
     */
    private var callBack: CallBack
) : Thread() {

    override fun run() {
        var connection: HttpURLConnection? = null
        try {
            val url = URL(checkUpdateUrl)
            if (checkUpdateUrl.startsWith("https//")) {
                TrustAllCertificates.install()
            }
            connection = url.openConnection() as HttpURLConnection?
            if (isPost) {
                val sb = StringBuffer("")
                if (postParams != null) {
                    val set = postParams!!.entries
                    val iterator = set.iterator()
                    while (iterator.hasNext()) {
                        val map = iterator.next()
                        sb.append(map.key)
                        sb.append("=")
                        sb.append(map.value)
                        if (iterator.hasNext()) {
                            sb.append("&")
                        }
                    }
                }
                val urlParamsStr = sb.toString()
                val postData: ByteArray = urlParamsStr.toByteArray(Charset.forName("UTF-8"))
                val postDataLength: Int = postData.size

                connection!!.requestMethod = "POST"
                connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded")
                connection.setRequestProperty("charset", "utf-8")
                connection.setRequestProperty("Content-Length", postDataLength.toString())
                connection.doOutput = true
                connection.useCaches = false

                val wr = DataOutputStream(connection.outputStream)
                wr.write(postData)
                wr.flush()
            }
            val inputStream: InputStream = BufferedInputStream(connection!!.inputStream)
            val data = read(inputStream)
            inputStream.close()

            try {
                val versionBean = VersionBean()
                versionBean.parse(data)
                callBack.callBack(
                    versionBean,
                    hasNewVersion(
                        PackageUtils().getVersionCode(mContext!!),
                        versionBean.versionCode
                    )
                )
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                callBack.callBack(null, false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            callBack.callBack(null, false)
        } finally {
            if (connection != null) {
                connection.disconnect()
            }
        }


    }

    private fun hasNewVersion(old: Int, n: Int): Boolean {
        return old < n
    }


    private fun read(inputStream: InputStream): String {
        val outStream = ByteArrayOutputStream()

        var b: Int = inputStream.read()
        while (b != -1) {
            outStream.write(b)
            b = inputStream.read()
        }
        return outStream.toString()
    }

    public interface CallBack {
        fun callBack(versionBean: VersionBean?, hasNewVersion: Boolean)
    }
}