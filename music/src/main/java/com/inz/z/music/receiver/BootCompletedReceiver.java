package com.inz.z.music.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.inz.z.music.view.activity.MainActivity;

/**
 * 启动完成后广播接收
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/21 15:26.
 */
public class BootCompletedReceiver extends BroadcastReceiver {
    private static final String TAG = "BootCompletedReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: Action = " + intent.getAction());
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // 系统开启动完成
            Intent startBootActivity = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startBootActivity);
        }

    }
}
