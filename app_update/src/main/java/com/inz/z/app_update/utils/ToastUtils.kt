package com.inz.z.app_update.utils

import android.content.Context
import android.support.annotation.StringRes
import android.widget.Toast

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/13 11:54.
 */
class ToastUtils {

    companion object {

        fun show(context: Context, @StringRes stringRes: Int) {
            Toast.makeText(context, stringRes, Toast.LENGTH_SHORT).show()
        }

        fun show(context: Context, content: String) {
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
        }
    }
}