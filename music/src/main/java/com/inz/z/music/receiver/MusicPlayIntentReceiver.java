package com.inz.z.music.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.inz.z.base.util.L;
import com.inz.z.music.base.Constants;
import com.inz.z.music.service.MusicPlayService;

/**
 * 音乐播放广播状态接收
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/21 13:59.
 */
public class MusicPlayIntentReceiver extends BroadcastReceiver {
    private static final String TAG = "MusicPlayIntentReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {
        MusicPlayService musicPlayService = getMusicPlayService(context);
        String action = intent.getAction();
        L.i(TAG, "onReceive: Action = " + action);
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(action)) {
            // 用户拔出耳机或断开
            if (musicPlayService != null) {
                musicPlayService.pause();
                L.i(TAG, "action : ACTION_AUDIO_BECOMING_NOISY - to Pause");
            }
        } else if (AudioManager.ACTION_HEADSET_PLUG.equals(action)) {
            // 耳机插入状态
            if (intent.hasExtra("state")) {
                // 0: 未插入，1：已插入
                int state = intent.getIntExtra("state", 0);
                if (state == 1) {
                    String name = intent.getStringExtra("name");
                    // 1 ： 有麦克风，0：无麦克风
                    int microphone = intent.getIntExtra("microphone", 0);
                    L.i(TAG, "onReceive: name = " + name + " ; microphone = " + microphone);
                }
                L.i(TAG, "onReceive:  state = " + state);
            }
        } else if (Constants.MUSIC_PLAY_ACTON.equals(action)) {
            // 音乐播放状态
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                parseData(bundle);
            }
        }


    }


    /**
     * 解析数据
     *
     * @param bundle 数据
     */
    private void parseData(@NonNull Bundle bundle) {

    }

    /**
     * 获取音乐播放Service
     *
     * @param context 上下文
     * @return MusicPlayService
     */
    private MusicPlayService getMusicPlayService(Context context) {
        Intent musicPlayServiceIntent = new Intent();
        musicPlayServiceIntent.setClass(context, MusicPlayService.class);
        MusicPlayService.MusicPlayBinder musicPlayBinder = (MusicPlayService.MusicPlayBinder) peekService(context, musicPlayServiceIntent);
        if (musicPlayBinder != null) {
            return musicPlayBinder.getService();
        }
        return null;
    }

}
