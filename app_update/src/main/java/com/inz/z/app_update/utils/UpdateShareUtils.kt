package com.inz.z.app_update.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/14 17:27.
 */
class UpdateShareUtils {

    companion object {
        private const val APP_UPDATE_SP = "app_update_up"

        /**
         * 保存更新时间
         */
        fun saveUpdateTime(context: Context, updateTime: Long) {
            val sp = context.getSharedPreferences(APP_UPDATE_SP, Context.MODE_PRIVATE)
            val edit = sp.edit()
            edit.putLong("update_time", updateTime)
            edit.apply()
        }

        /**
         * 获取更新时间
         */
        fun getUpdateTime(context: Context): Long {
            val sp = context.getSharedPreferences(APP_UPDATE_SP, Context.MODE_PRIVATE)
            return sp.getLong("update_time", 0L)
        }

        /**
         * 保存是否显示更新提示
         * @param isShowUpdate 是否显示
         */
        fun saveIsShowUpdate(context: Context, isShowUpdate: Boolean) {
            val sp = context.getSharedPreferences(APP_UPDATE_SP, Context.MODE_PRIVATE)
            val edit = sp.edit()
            edit.putBoolean("is_show_update_dialog", false)
            edit.apply()
        }

        /**
         * 获取是否显示更新框
         */
        fun getIsShowUpdate(context: Context): Boolean {
            val sp = context.getSharedPreferences(APP_UPDATE_SP, Context.MODE_PRIVATE)
            return sp.getBoolean("is_show_update_dialog", false)
        }

        /**
         * 保存是否显示下载提示
         * @param isShowDownload 是否显示
         */
        fun saveIsShowDownload(context: Context, isShowDownload: Boolean) {
            val sp = context.getSharedPreferences(APP_UPDATE_SP, Context.MODE_PRIVATE)
            val edit = sp.edit()
            edit.putBoolean("is_show_download_dialog", isShowDownload)
            edit.apply()
        }

        /**
         * 获取是否显示下载
         */
        fun getIsShowDownload(context: Context): Boolean {
            val sp = context.getSharedPreferences(APP_UPDATE_SP, Context.MODE_PRIVATE)
            return sp.getBoolean("is_show_download_dialog", false)
        }

        /**
         * 保存更新状态
         * @param loading 是否正在下载
         */
        fun saveUpdateStatus(context: Context, loading: Boolean) {
            val sp = context.getSharedPreferences(APP_UPDATE_SP, Context.MODE_PRIVATE)
            val edit = sp.edit()
            edit.putBoolean("loading", loading)
            edit.apply()
        }

        /**
         * 获取更新状态 : 默认为未下载
         */
        fun getUpdateStatus(context: Context): Boolean {
            val sp = context.getSharedPreferences(APP_UPDATE_SP, Context.MODE_PRIVATE)
            return sp.getBoolean("loading", false)
        }

        /**
         * 保存是否跳过
         * @param skip 是否跳过
         */
        fun saveIsSkip(context: Context, skip: Boolean) {
            val sp = context.getSharedPreferences(APP_UPDATE_SP, Context.MODE_PRIVATE)
            val edit = sp.edit()
            edit.putBoolean("is_skip", skip)
            edit.apply()
        }

        /**
         * 获取是否跳过此版本
         */
        fun getIsSkip(context: Context): Boolean {
            val sp = context.getSharedPreferences(APP_UPDATE_SP, Context.MODE_PRIVATE)
            return sp.getBoolean("is_skip", false)
        }

        /**
         * 保存跳过版本号
         * @param versionCode 版本号
         */
        fun saveSkipVersion(context: Context, versionCode: Int) {
            val sp = context.getSharedPreferences(APP_UPDATE_SP, Context.MODE_PRIVATE)
            val edit = sp.edit()
            edit.putInt("skip_version_code", versionCode)
            edit.apply()
        }

        /**
         * 获取跳过版本：默认versionCode = 1
         */
        fun getSkipVersion(context: Context): Int {
            val sp = context.getSharedPreferences(APP_UPDATE_SP, Context.MODE_PRIVATE)
            return sp.getInt("skip_version_code", 1)
        }

        /**
         * 保存APK 文件是否 安装
         */
        fun saveInstallApk(context: Context, isInstall: Boolean) {
            val sp = context.getSharedPreferences(APP_UPDATE_SP, Context.MODE_PRIVATE)
            val edit = sp.edit()
            edit.putBoolean("isInstall", isInstall)
            edit.apply()
        }

        /**
         * 获取是否安装APK: 默认已安装
         */
        fun getInstallApk(context: Context): Boolean {
            val sp = context.getSharedPreferences(APP_UPDATE_SP, Context.MODE_PRIVATE)
            return sp.getBoolean("isInstall", true)
        }

        /**
         * 保存APK 文件是否已下载
         */
        fun saveDownloadApk(context: Context, isDownload: Boolean) {
            val sp = context.getSharedPreferences(APP_UPDATE_SP, Context.MODE_PRIVATE)
            val edit = sp.edit()
            edit.putBoolean("isDownload", isDownload)
            edit.apply()
        }

        /**
         * 获取 Apk 文件是否已经 下载
         */
        fun getDownloadApk(context: Context): Boolean {
            val sp = context.getSharedPreferences(APP_UPDATE_SP, Context.MODE_PRIVATE)
            return sp.getBoolean("isDownload", false)
        }
    }

}