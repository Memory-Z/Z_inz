package com.inz.z.app_update.bean

import org.json.JSONObject
import java.io.Serializable

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/9 10:30.
 */
class VersionBean : Serializable {
    /**
     * 版本号
     */
    var versionCode: Int = 1
    /**
     * 版本名
     */
    var versionName: String = ""
    /**
     * 更新内容
     */
    var content: String = ""
    /**
     * 最小版本
     */
    var minSupport: Int = 1
    /**
     * 下载地址
     */
    var url: String = ""


    /**
     * 解析请求结果
     */
    public fun parse(jsonStr: String): VersionBean {
        val json = JSONObject(jsonStr)
        versionCode = json.getInt("versionCode")
        versionName = json.getString("versionName")
        content = json.getString("content")
        minSupport = json.getInt("minSupport")
        url = json.getString("url")
        return this
    }
}