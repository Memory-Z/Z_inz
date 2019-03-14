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


    private var sp: SharedPreferences? = null

    companion object {
        private const val APP_UPDATE_SP = "app_update_up"

        /**
         * 保存更新时间
         */
        @SuppressLint("CommitPrefEdits")
        fun saveUpdateTime(context: Context, updateTime: Long) {
            val sp = context.getSharedPreferences(APP_UPDATE_SP, Context.MODE_PRIVATE)
            val edit = sp.edit()
            edit.putLong("updateTime", updateTime)
            edit.apply()
        }

        /**
         * 获取更新时间
         */
        fun getUpdateTime(context: Context): Long {
            val sp = context.getSharedPreferences(APP_UPDATE_SP, Context.MODE_PRIVATE)
            return sp.getLong("updateTime", 0L)
        }
    }

    private fun getSharedPreference(context: Context): SharedPreferences? {
        sp = context.getSharedPreferences(APP_UPDATE_SP, Context.MODE_PRIVATE)
        return sp as SharedPreferences?
    }

}