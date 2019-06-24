package com.inz.z.music.service;

import android.app.Service;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.inz.z.music.base.Constants;
import com.inz.z.music.receiver.MusicPlayIntentReceiver;
import com.inz.z.music.view.adapter.ItemSongsBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/20 15:15.
 */
public class MusicPlayService extends Service {

    private static final String TAG = "MusicPlayService";
    private static final String LOCK_TAG = "inz:music:PlayService";

    private MusicPlayBinder playBinder;
    private Context mContext;
    private List<MusicPlayService.OnPlayStatusListener> playStatusListenerList;
    private List<MediaPlayer> mediaPlayerList;
    private MediaPlayer currentMediaPlayer, nextMediaPlayer;
    private int currentPosition = 0;
    private PowerManager.WakeLock wakeLock;
    private WifiManager.WifiLock wifiLock;

    private final static AudioAttributes MUSIC_AUDIO_ATTR;

    static {
        MUSIC_AUDIO_ATTR = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .build();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (playBinder == null) {
            playBinder = new MusicPlayBinder();
        }
        mContext = getApplicationContext();
        initLock();
        registerMusicPlayReceiver();
        return playBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        if (wakeLock == null || wifiLock == null) {
            initLock();
        }
        if (localBroadcastManager == null) {
            registerMusicPlayReceiver();
        }
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {

        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        releaseLock();
        unregisterMusicPlayReceiver();
        super.onDestroy();
    }

    /**
     * 初始化锁
     */
    private void initLock() {
        PowerManager powerManager = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, LOCK_TAG);
        wakeLock.setReferenceCounted(false);
        wakeLock.acquire(4 * 60);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, LOCK_TAG);
        wifiLock.setReferenceCounted(false);
        wifiLock.acquire();
    }

    /**
     * 请求锁
     */
    private void acquireLock() {
        if (wifiLock == null || wakeLock == null) {
            initLock();
            return;
        }
        if (!wifiLock.isHeld()) {
            wifiLock.acquire();
        }
        if (!wakeLock.isHeld()) {
            wakeLock.acquire(4 * 60);
        }
    }

    /**
     * 释放锁
     */
    private void releaseLock() {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        if (wifiLock != null && wifiLock.isHeld()) {
            wifiLock.release();
        }
    }


    public void start() {
        // 请求锁
        acquireLock();
        if (currentMediaPlayer != null) {
            currentMediaPlayer.start();
        } else if (currentPosition + 1 < mediaPlayerList.size()) {
            MediaPlayer mediaPlayer = mediaPlayerList.get(currentPosition + 1);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    currentMediaPlayer = mp;
                }
            });
            try {
                mediaPlayer.setDataSource("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("No Position .");
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        if (currentMediaPlayer != null) {
            currentMediaPlayer.pause();
        }
        releaseLock();
    }

    public void setNextPlayerFilePath(String filePath) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(MUSIC_AUDIO_ATTR);
        try {
            mediaPlayer.setDataSource(filePath);
            setNextPlayer(mediaPlayer);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "setNextPlayerFilePath: mediaPlayer.setDataSource", e);
        }
    }

    public void setNextPlayerUri(Uri uri) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(MUSIC_AUDIO_ATTR);
        try {
            mediaPlayer.setDataSource(mContext, uri);
            setNextPlayer(mediaPlayer);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "setNextPlayerUri: mediaPlayer.setDataSource.", e);
        }

    }

    /**
     * 设置下一个播放
     *
     * @param mediaPlayer 播放
     */
    private void setNextPlayer(MediaPlayer mediaPlayer) {
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        currentMediaPlayer.setNextMediaPlayer(mediaPlayer);
    }

    /**
     * 添加MediaPlayer
     *
     * @param mediaPlayer 播放器
     */
    private void addMediaPlayer(MediaPlayer mediaPlayer) {
        if (mediaPlayerList == null) {
            mediaPlayerList = new ArrayList<>();
        }
        mediaPlayerList.add(mediaPlayer);
    }

    private MusicPlayIntentReceiver musicPlayIntentReceiver;
    private LocalBroadcastManager localBroadcastManager;

    /**
     * 注册广播接收
     */
    private void registerMusicPlayReceiver() {
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.MUSIC_PLAY_ACTON);
        intentFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        intentFilter.addAction(AudioManager.ACTION_HEADSET_PLUG);
        intentFilter.addAction(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED);
        musicPlayIntentReceiver = new MusicPlayIntentReceiver();
        localBroadcastManager.registerReceiver(musicPlayIntentReceiver, intentFilter);
    }

    /**
     * 解除广播接收
     */
    private void unregisterMusicPlayReceiver() {
        if (localBroadcastManager != null && musicPlayIntentReceiver != null) {
            localBroadcastManager.unregisterReceiver(musicPlayIntentReceiver);
            musicPlayIntentReceiver = null;
        }
    }

    /**
     * 发送广播消息
     */
    private void sendMusicPlayMessage(Intent intent) {
        if (localBroadcastManager != null) {
            localBroadcastManager.sendBroadcastSync(intent);
        }
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
     * 播放状态错误
     */
    public enum PlayStatusError {
        NO_LIST,
        START_ERROR,
        PAUSE_ERROR,
        PLAY_NEXT_ERROR;
    }

    /**
     * 播放类型
     */
    public enum PlayType {
        SINGE_CYCLE,
        LIST_CYCLE,
        RANDOM,
        ORDER;
    }

    /**
     * 播放监听
     */
    public interface OnPlayStatusListener {

        void start();

        void pause();

        void error(String message, Throwable e);

        void playList(List<ItemSongsBean> songsBeanList, int position, boolean isPlay, PlayType type);
    }

    private class MediaPlayListener implements MediaPlayer.OnInfoListener,
            MediaPlayer.OnPreparedListener,
            MediaPlayer.OnSeekCompleteListener,
            MediaPlayer.OnErrorListener {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            return false;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {

        }

        @Override
        public void onSeekComplete(MediaPlayer mp) {

        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }
    }


}
;