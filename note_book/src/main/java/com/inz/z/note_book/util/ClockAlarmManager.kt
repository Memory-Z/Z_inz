package com.inz.z.note_book.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2020/01/15 16:33.
 */
object ClockAlarmManager {


    private fun getAlarmManager(context: Context): AlarmManager? {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        if (alarmManager != null) {
            return alarmManager
        }
        return null
    }

//    public fun getNextAlarm(context: Context) {
//        val alarmManager = getAlarmManager(context)
//        val clock = alarmManager?.nextAlarmClock
//        if (clock != null) {
//
//        }
//    }

    public fun addClockAlarm(context: Context, triggerAtMillis: Long, operation: PendingIntent) {
        val alarmManager = getAlarmManager(context)
        alarmManager?.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerAtMillis,
            AlarmManager.INTERVAL_DAY,
            operation
        )
    }

}