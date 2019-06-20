package com.inz.z.music.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/20 15:15.
 */
public class MusicPlayService extends Service {

    private static final String TAG = "MusicPlayService";

    private MusicPlayBinder playBinder;
    private List<MusicPlayService.OnPlayStatusListener> playStatusListenerList;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (playBinder == null) {
            playBinder = new MusicPlayBinder();
        }
        return playBinder;
    }

    private void initMediaPlayer() {

    }

    private void initAudio() {

    }

    public class MusicPlayBinder extends Binder {

        public MusicPlayService getService() {
            return MusicPlayService.this;
        }
    }

    public void addOnPlayStatusListener(@NonNull MusicPlayService.OnPlayStatusListener listener) {
        if (playStatusListenerList == null) {
            playStatusListenerList = new ArrayList<>(16);
        }
        this.playStatusListenerList.add(listener);
    }


    public void removeOnPlayStatusListener(@NonNull MusicPlayService.OnPlayStatusListener listener) {
        if (this.playStatusListenerList != null) {
            this.playStatusListenerList.remove(listener);
        }
    }

    public void clearOnPlayStatusListeners() {
        if (this.playStatusListenerList != null) {
            this.playStatusListenerList.clear();
        }
    }

    /**
     * 播放监听
     */
    public interface OnPlayStatusListener {

        void play();

        void stop();
    }
}
