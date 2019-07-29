package com.inz.z.screen_record.view

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.inz.z.base.util.FileUtils

import com.inz.z.other_module.R
import com.inz.z.screen_record.service.BitmapConnectionService
import com.inz.z.screen_record.service.SocketService
import kotlinx.android.synthetic.screen_record.receiver_show_activity.*

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/16 09:19.
 */
class ReceiverShowActivity : AppCompatActivity() {
    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.receiver_show_activity)
        mContext = this
        initView()
        initData()
    }

    companion object {
        private val TAG = "ReceiverShowActivity"
        private var service: SocketService? = null
    }

    private fun initView() {
        receiver_show_start_btn.setOnClickListener {
            val ip = receiver_show_ip_et.text.toString()
            val port = receiver_show_port_et.text.toString().toInt();
            service?.connectServerSocket(ip, port)
        }
    }

    private fun bindService() {
        val service = Intent(mContext, SocketService::class.java)
        bindService(service, ConnectServiceConnection(), Service.BIND_AUTO_CREATE)
    }


    private inner class ConnectServiceConnection : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            service = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder: SocketService.SocketServiceBinder =
                service as SocketService.SocketServiceBinder
            ReceiverShowActivity.service = binder.getService()
            ReceiverShowActivity.service!!.socketServiceListener =
                object : SocketService.SocketServiceListener {
                    override fun createServerSocket(ip: String, port: Int) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun createServerSocketFailure(msg: String, e: Throwable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun connectClientSocket() {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun connectClientSocketFailure(msg: String, e: Throwable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun clientReceiveMsg(bitmap: Bitmap) {
                        runOnUiThread {
                            Glide.with(mContext!!).load(bitmap).into(receiver_show_iv)
                        }
                    }

                }
        }
    }

    private fun initData() {
        bindService()
    }
}
