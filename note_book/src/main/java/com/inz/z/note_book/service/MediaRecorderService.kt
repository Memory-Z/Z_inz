package com.inz.z.note_book.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.IBinder
import com.inz.z.base.util.L
import java.util.concurrent.atomic.AtomicBoolean

/**
 *
 * 录音 Service
 * @author 11654
 * @version 1.0.0
 * Create by inz in 2019/12/7 16:13
 **/
class MediaRecorderService : Service() {
    companion object {
        const val TAG = "MediaRecorderService"
    }

    private var mediaRecorder: MediaRecorder? = null
    /**
     * 是否正在录制中/ 默认否；
     */
    private var isRecorder: AtomicBoolean = AtomicBoolean(false)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    private fun initMediaRecorder(mContext: Context) {
        mediaRecorder = MediaRecorder()
                .apply {
                    // 设置音频来源
                    setAudioSource(MediaRecorder.AudioSource.VOICE_CALL)
                    // 设置音频输出格式
                    setOutputFormat(MediaRecorder.OutputFormat.AMR_WB)
                    // 设置音频文件的编码
                    setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB)
                    setOnInfoListener(object : MediaRecorder.OnInfoListener {
                        override fun onInfo(mr: MediaRecorder?, what: Int, extra: Int) {
                            when (what) {
                                MediaRecorder.MEDIA_RECORDER_INFO_UNKNOWN -> {
                                    // 未知错位
                                }
                                MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED -> {
                                    // 录制到达最大时长
                                }
                                MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED -> {
                                    // 录制文件超过指定大小。需要 setNextOutputFile(File) 指定一个新的文件进行存储
                                }
                                MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_APPROACHING -> {
                                    // 录制文件接近最大大小
                                }
                            }
                        }
                    })
                    setOnErrorListener(
                            object : MediaRecorder.OnErrorListener {
                                override fun onError(mr: MediaRecorder?, what: Int, extra: Int) {
                                    when (what) {
                                        MediaRecorder.MEDIA_RECORDER_ERROR_UNKNOWN -> {
                                            // 未知错误
                                        }
                                        MediaRecorder.MEDIA_ERROR_SERVER_DIED -> {
                                            // 媒体服务卡死
                                        }
                                    }
                                }
                            }
                    )
                }
    }

    /**
     * 开始录制音频文件
     * @param filePath 文件地址
     */
    private fun startRecorder(filePath: String) {
        L.i(TAG, "startRecorder: filePath = " + filePath)
        if (isRecorder.get()) {
            L.w(TAG, "startRecorder: is recording . ")
            return
        }
        if (mediaRecorder != null) {
            mediaRecorder!!.apply {
                setOutputFile(filePath)
                try {
                    prepare()
                    start()
                    isRecorder.set(true)
                } catch (e: Exception) {
                    L.e(TAG, "media recorder start failure. ", e)
                }
            }
        }
    }

    /**
     * 结束录制
     */
    private fun stopRecorder() {
        if (mediaRecorder != null && isRecorder.get()) {
            mediaRecorder!!.stop()
            mediaRecorder!!.reset()
            isRecorder.set(false)
        }
    }
}