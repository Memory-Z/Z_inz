package com.inz.z.music.service;

import android.app.Service;
import android.bluetooth.BluetoothHeadset;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.inz.z.base.util.L;
import com.inz.z.music.base.Constants;
import com.inz.z.music.database.ItemSongsBean;
import com.inz.z.music.database.ItemSongsBeanDao;
import com.inz.z.music.receiver.MusicPlayIntentReceiver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private Boolean haveListener = false;
    private List<ItemSongsBean> itemSongsBeanList;
    private MediaPlayer currentMediaPlayer, nextMediaPlayer;
    private int currentPosition = 0;
    private PowerManager.WakeLock wakeLock;
    private WifiManager.WifiLock wifiLock;

    private Handler musicPlayHandler;
    private ScheduledExecutorService scheduledExecutorService;

    /**
     * 播放类型
     */
    private PlayType playType = PlayType.ORDER;

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
        initConfiguration();
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
        initConfiguration();
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
     * 配置属性
     */
    private void initConfiguration() {
        if (musicPlayHandlerCallback == null) {
            musicPlayHandlerCallback = new MusicPlayHandlerCallback();
        }
        if (musicPlayHandler == null) {
            musicPlayHandler = new Handler(musicPlayHandlerCallback);
        }
        if (scheduledExecutorService == null) {
            scheduledExecutorService = Executors.newScheduledThreadPool(16);
            scheduledExecutorService.scheduleAtFixedRate(new SeekRunnable(), 500, 500, TimeUnit.MILLISECONDS);
        }
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


    /**
     * 开始播放
     */
    public void play() {
        L.i(TAG, "MusicPlayService: play -> isPrepared = " + isPrepared
                + " ; mediaPlayer = " + currentMediaPlayer + " ; currentPosition = "
                + currentPosition);
        if (!isPrepared) {
            for (OnPlayStatusListener listener : playStatusListenerList) {
                listener.error("player start error : not prepared.", PlayStatusError.NO_PREPARED);
            }
            return;
        }
        // 请求锁
        acquireLock();
        if (currentMediaPlayer != null) {
            boolean isPlaying = currentMediaPlayer.isPlaying();
            if (!isPlaying) {
                currentMediaPlayer.start();
            }
        } else {
            for (OnPlayStatusListener listener : playStatusListenerList) {
                listener.error("player start error : media player is null .", PlayStatusError.START_ERROR);
            }
            L.wtf(TAG, "No mediaPlayer to play.");
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        L.i(TAG, "MusicPlayService: pause -> ");
        if (currentMediaPlayer != null) {
            currentMediaPlayer.pause();
        }
        releaseLock();
    }

    /**
     * 上一曲
     */
    public void previous() {
        L.i(TAG, "MusicPlayService: previous -> ");
        if (itemSongsBeanList != null && itemSongsBeanList.size() > 0) {
            if (currentMediaPlayer != null) {
                currentPosition -= 1;
                if (currentPosition < 0) {
                    currentPosition = itemSongsBeanList.size() - 1;
                }
                currentMediaPlayer.reset();
                ItemSongsBean songsBean = itemSongsBeanList.get(currentPosition);
                // 设置MediaPlayer 数据
                setMediaPlayerData(currentMediaPlayer, songsBean);
            }
        } else {
            for (OnPlayStatusListener listener : playStatusListenerList) {
                listener.error("player to previous error: list is null or siz is 0.", PlayStatusError.NO_LIST);
            }
        }
    }

    /**
     * 下一曲
     */
    public void next() {
        L.i(TAG, "MusicPlayService: next -> ");
        if (itemSongsBeanList != null && itemSongsBeanList.size() > 0) {
            if (currentMediaPlayer != null) {
                currentPosition += 1;
                if (currentPosition == itemSongsBeanList.size()) {
                    currentPosition = 0;
                }
                currentMediaPlayer.reset();
                ItemSongsBean songsBean = itemSongsBeanList.get(currentPosition);
                // 设置MediaPlayer 数据
                setMediaPlayerData(currentMediaPlayer, songsBean);
            }
        } else {
            for (OnPlayStatusListener listener : playStatusListenerList) {
                listener.error("player to next error: list is null or siz is 0.", PlayStatusError.NO_LIST);
            }
        }
    }

    /**
     * 是否准备完成
     */
    private boolean isPrepared = false;
    /**
     * 是否自动播放
     */
    private boolean isAutoPlay = false;

    /**
     * 设置MediaPlayer 数据
     *
     * @param mediaPlayer   MediaPlayer
     * @param itemSongsBean 数据
     */
    private void setMediaPlayerData(MediaPlayer mediaPlayer, ItemSongsBean itemSongsBean) {
        mediaPlayer.setAudioAttributes(MUSIC_AUDIO_ATTR);

        String filePath = "";//itemSongsBean.getFilePath();
        try {
            String fileUri = filePath;// itemSongsBean.getFileUri();
            if (!"".equals(fileUri)) {
                Uri uri = Uri.parse(fileUri);
                mediaPlayer.setDataSource(mContext, uri);
            } else {
                mediaPlayer.setDataSource(filePath);
            }
        } catch (IOException e) {
            L.e(TAG, "start: ", e);
            for (OnPlayStatusListener listener : playStatusListenerList) {
                listener.error("player start error : path or uri error.", PlayStatusError.START_ERROR);
            }
            return;
        }
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                isPrepared = true;
                for (OnPlayStatusListener listener : playStatusListenerList) {
                    listener.prepared(isPrepared, mp.getDuration());
                }
            }
        });
    }

    public void setNextPlayerFilePath(String filePath) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(MUSIC_AUDIO_ATTR);
        try {
            mediaPlayer.setDataSource(filePath);
            setNextPlayer(mediaPlayer);
        } catch (IOException e) {
            L.e(TAG, "setNextPlayerFilePath: mediaPlayer.setDataSource", e);
        }
    }

    public void setNextPlayerUri(Uri uri) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(MUSIC_AUDIO_ATTR);
        try {
            mediaPlayer.setDataSource(mContext, uri);
            setNextPlayer(mediaPlayer);
        } catch (IOException e) {
            L.e(TAG, "setNextPlayerUri: mediaPlayer.setDataSource.", e);
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
     * @param itemSongsBean 播放器
     */
    public void addItemSongsBean(ItemSongsBean itemSongsBean) {
        if (itemSongsBeanList == null) {
            itemSongsBeanList = new ArrayList<>();
        }
        itemSongsBeanList.add(itemSongsBean);
        notifyDataChange();
    }

    /**
     * 添加数据
     *
     * @param itemSongsBeans 数据列表
     */
    public void addItemSongsBeanList(List<ItemSongsBean> itemSongsBeans) {
        if (itemSongsBeanList == null) {
            itemSongsBeanList = new ArrayList<>();
        }
        itemSongsBeanList.addAll(itemSongsBeans);
        notifyDataChange();

    }

    /**
     * 同步数据改变
     */
    private void notifyDataChange() {
        if (itemSongsBeanList != null && itemSongsBeanList.size() > 0) {
            if (currentMediaPlayer == null) {
                currentMediaPlayer = new MediaPlayer();
                isPrepared = false;
                currentPosition = 0;
                ItemSongsBean songsBean = itemSongsBeanList.get(currentPosition);
                setMediaPlayerData(currentMediaPlayer, songsBean);
                for (OnPlayStatusListener listener : playStatusListenerList) {
                    listener.prepared(isPrepared, 0);
                }
            }
        } else {
            if (currentMediaPlayer != null) {
                currentMediaPlayer.reset();
                currentMediaPlayer.release();
            }
            currentMediaPlayer = null;
        }
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
        if (!haveListener) {
            scheduledExecutorService.execute(new GetMusicListRunnable());
            haveListener = true;
        }
    }

    public void removeOnPlayStatusListener(@NonNull MusicPlayService.OnPlayStatusListener listener) {
        if (this.playStatusListenerList != null) {
            this.playStatusListenerList.remove(listener);
            if (this.playStatusListenerList.size() == 0) {
                this.haveListener = false;
            }
        }
    }

    public void clearOnPlayStatusListeners() {
        if (this.playStatusListenerList != null) {
            this.playStatusListenerList.clear();
        }
        haveListener = false;
    }

    /**
     * 播放状态错误
     */
    public enum PlayStatusError {
        NO_PREPARED,
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

        void getAudioList();

        void prepared(boolean isPrepared, int duration);

        void onPlay();

        void onPause();

        void error(String message, PlayStatusError e);

        void seekBar(long current, long duration);
    }

    private class MediaPlayListener implements MediaPlayer.OnInfoListener,
            MediaPlayer.OnSeekCompleteListener,
            MediaPlayer.OnErrorListener {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            return false;
        }

        @Override
        public void onSeekComplete(MediaPlayer mp) {

        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }
    }


    private MusicPlayHandlerCallback musicPlayHandlerCallback;

    /**
     * 进度条状态
     */
    private class SeekRunnable implements Runnable {
        @Override
        public void run() {
            if (isPrepared && currentMediaPlayer != null) {
                int currentPosition = currentMediaPlayer.getCurrentPosition();
                int duration = currentMediaPlayer.getDuration();
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt("currentPosition", currentPosition);
                bundle.putInt("duration", duration);
                message.setData(bundle);
                message.what = HANDLER_SEEK;
                if (musicPlayHandler != null) {
                    musicPlayHandler.sendMessage(message);
                }
            }
        }
    }

    private class GetMusicListRunnable implements Runnable {
        @Override
        public void run() {
            for (OnPlayStatusListener listener : playStatusListenerList) {
                listener.getAudioList();
            }

        }
    }

    private static final int HANDLER_SEEK = 0x0010;
    private static final int HANDLER_STATUS = 0x0011;

    /**
     * Music play callback ，get seek
     */
    private class MusicPlayHandlerCallback implements Handler.Callback {
        @Override
        public boolean handleMessage(Message msg) {
            int what = msg.what;
            Bundle bundle = msg.getData();
            switch (what) {
                case HANDLER_SEEK:
                    int currentPosition = bundle.getInt("currentPosition", 0);
                    int duration = bundle.getInt("duration", 0);
                    for (OnPlayStatusListener listener : playStatusListenerList) {
                        listener.seekBar(currentPosition, duration);
                    }
                    break;
                case HANDLER_STATUS:
                    break;
                default:
                    break;
            }
            return false;
        }
    }

}