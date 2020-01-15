package com.inz.z.note_book.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.inz.z.base.util.L
import com.inz.z.note_book.util.Constants

/**
 * 时钟 广播
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2020/01/15 15:51.
 */
class ClockAlarmBroadcast : BroadcastReceiver() {
    companion object {
        private const val TAG = "AlarmBroadcast"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        L.i(TAG, "onReceive: $action <<<<<< ")
        when (action) {
            Constants.CLOCK_ALARM_START_ACTION -> {

            }
            else -> {

            }
        }
    }


}