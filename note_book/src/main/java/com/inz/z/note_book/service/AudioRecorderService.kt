package com.inz.z.note_book.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.IBinder
import com.inz.z.base.util.L
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.atomic.AtomicBoolean

/**
 *
 * 录音 服务
 * @author 11654
 * @version 1.0.0
 * Create by inz in 2019/12/7 17:21
 **/
class AudioRecorderService : Service() {
    companion object {
        const val TAG = "AudioRecorderService"
    }

    private var mAudioRecord: AudioRecord? = null
    /**
     * 是否正在录制中.
     */
    private var isRecording = AtomicBoolean(false)
    private var bufferSize = 2048

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun initAudioRecord(context: Context) {

        val simpleRateInHz = 10000
        val channelConfig = AudioFormat.CHANNEL_OUT_SURROUND
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT
        val bufferSizeBytes = AudioRecord.getMinBufferSize(simpleRateInHz, channelConfig, audioFormat)
        bufferSize = bufferSizeBytes
        mAudioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                simpleRateInHz,
                channelConfig,
                audioFormat,
                bufferSizeBytes
        )
    }

    private fun startRecorder(filePath: String) {
        val tmpFile = File(filePath)
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(tmpFile)
            mAudioRecord?.startRecording()
            val byteBuffer = ByteArray(bufferSize)
            // TODO save audio to File .
        } catch (e: Exception) {
            L.e(TAG, "start recorder . failure. ", e)
        }


    }
}