package com.inz.z.z_demo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.inz.z.base.util.L
import com.inz.z.base.view.AbsBaseActivity
import kotlinx.android.synthetic.main.main_activity.*
import java.net.URL

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/14 14:38.
 */
class MainActivity : AbsBaseActivity() {
    companion object {
        const val TAG = "MainActivity"
        const val MUSIC_URL = "http://audio04.dmhmusic.com/71_53_T10041163905_128_4_1_0_sdk-cpm/cn/0209/M00/2E/56/ChR461pkTq-Aa3DjADzFjCp9OWk181.mp3?xcode=eee50cb2f0a6027f58a973794bc726c453fc833"
    }

    private var mediaPlayer: MediaPlayer? = null
    /**
     * 是否正在播放
     */
    private var isPlaying = false

    override fun initWindow() {

    }

    override fun getLayoutId(): Int {
        return R.layout.main_activity
    }

    override fun initView() {
        main_music_play_ibtn.setOnClickListener {
            if (isPlaying) {
                pause()

            } else {
                play()
            }
        }
    }

    override fun initData() {
    }


    override fun onResume() {
        super.onResume()
//        createMediaPlayer(MUSIC_URL)

    }

    val PERMISSIONS =
            arrayOf(
                    Manifest.permission.CALL_PHONE
            )

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
        } else {
            packageManager.checkPermission(
                    Manifest.permission.CALL_PHONE,
                    packageName
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun call(v: View) {
        L.i(TAG, "call --- ")
        if (checkPermission()) {
            val tellPhoneStr = main_tell_et.text.toString()
            val intent = Intent(Intent.ACTION_CALL)
            intent.setData(Uri.parse("tel:$tellPhoneStr"))
            startActivity(intent)
        } else {
            Toast.makeText(mContext, "请授予程序电话权限", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this, PERMISSIONS, 9999)
        }
    }

    fun callDial(v: View) {
        L.i(TAG, "callDial --- ")
        if (checkPermission()) {
            val tellPhoneStr = main_tell_et.text.toString()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:$tellPhoneStr"))
            startActivity(intent)
        } else {
            Toast.makeText(mContext, "请授予程序电话权限", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this, PERMISSIONS, 9999)
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 9999) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(mContext, "电话权限已获取", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 创建 媒体播放器
     */
    private fun createMediaPlayer(urlStr: String) {
        L.i(TAG, "createMediaPlayer: ")
        if (mediaPlayer == null) {
            val audioConfig = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .build()
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.apply {
                setAudioAttributes(audioConfig)
                isLooping = false
                setDataSource(urlStr)
                setOnCompletionListener {
                    L.i(TAG, "completion: ")
                    if (mediaPlayer != null) {
                        mediaPlayer!!.release()
                        mediaPlayer = null
//                        mediaPlayer!!.seekTo(0)
                    }
                    play()
                }
                setOnErrorListener(object : MediaPlayer.OnErrorListener {
                    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                        L.i(TAG, "onError: $what, $extra")
                        return false
                    }
                })
                setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
                    override fun onPrepared(mp: MediaPlayer?) {
                        L.i(TAG, "on prepared ")
//                        if (mediaPlayer != null ) {
//                            L.i(TAG, "seek ")
//                            val d = mediaPlayer!!.duration
////                            mediaPlayer!!.seekTo(d - 40)
//                        }
                        play()

                    }
                })
            }
            mediaPlayer!!.prepare()
            play()
            if (mediaPlayer != null) {
                L.i(TAG, "seek ")
                val d = mediaPlayer!!.duration
                mediaPlayer!!.seekTo(d - 40)
            }
        }
    }

    private fun play() {
        if (mediaPlayer == null) {
            L.i(TAG, "--- 0")
            createMediaPlayer(MUSIC_URL)
        } else
            L.i(TAG, "--- 1")
        if (!isPlaying && mediaPlayer != null) {
            L.i(TAG, "--- 2 ")
            mediaPlayer!!.start()
            isPlaying = true
        }
    }

    private fun pause() {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying && isPlaying) {
            mediaPlayer!!.pause()
            isPlaying = false
        }
    }

}