package com.inz.z.base.util

import android.util.Base64
import java.security.MessageDigest
import java.util.*

/**
 * 加密工具类
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/05/23 10:43.
 */
object EncryptUtil {

    /**
     * SHA-256 编码
     * @param value 需要加密的值
     */
    public fun encryptSHA256(value: String): String {
        val bts: ByteArray = value.toByteArray(Charsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-256")
        md.update(bts)
        return bytes2Hax(bts)
    }

    /**
     * byte 数组 转 16进制 字符串
     * @param bytes byte数组
     * @return 16进制 字符串
     */
    private fun bytes2Hax(bytes: ByteArray): String {
        val sb = StringBuffer()
        var temp: String
        for (b: Byte in bytes) {
            temp = b.toString(16)
            if (bytes.size == 1) {
                sb.append("0")
            }
            sb.append(temp)
        }
        return sb.toString()
    }

    /**
     * MD5 加密
     * @param value 需要加密的值
     * @return MD5 加密后的字符串
     */
    public fun encryptMD5(value: String): String {
        val bytes: ByteArray = value.toByteArray(Charsets.UTF_8)
        val md: MessageDigest = MessageDigest.getInstance("MD5")
        md.update(bytes)
        val bts: ByteArray = md.digest()
        val sb = StringBuffer()
        for (b: Byte in bts) {
            val s = b.toString(16)
            sb.append(s)
        }
        return sb.toString().toLowerCase(Locale.CHINA)
    }

    /**
     * Base64 加密
     * @param value 需要加密的值
     * @return 加密后的字符串
     */
    public fun encryptBase64(value: String): String {
        val bytes = Base64.encode(value.toByteArray(Charsets.UTF_8), Base64.DEFAULT)
        return bytes.toString()
    }

    /**
     * Base64 解密
     * @param value 加密后的字符串
     * @return 解密后的字符串
     */
    public fun decodeBase64(value: String): String {
        val bytes = Base64.decode(value, Base64.DEFAULT)
        return bytes.toString()
    }

}