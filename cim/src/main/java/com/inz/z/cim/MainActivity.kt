package com.inz.z.cim

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.farsunset.cim.sdk.android.*
import com.farsunset.cim.sdk.android.model.Message
import com.farsunset.cim.sdk.android.model.ReplyBody
import com.farsunset.cim.sdk.android.model.SentBody
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mContext: Context

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this

//        CIMPushManager.startService(mContext, intent)
        CIMListenerManager.registerMessageListener(object : CIMEventListener {
            override fun onReplyReceived(p0: ReplyBody?) {
                val tv = TextView(mContext)
                tv.text = "onReplyReceived"
                tv.setTextColor(Color.RED)
                main_message_ll.addView(tv)
            }

            override fun onConnectionFailed() {
                val tv = TextView(mContext)
                tv.text = "Failed"
                tv.setTextColor(Color.RED)
                main_message_ll.addView(tv)
            }

            override fun onMessageReceived(p0: Message?) {
                val message = p0.toString();
                val tv = TextView(mContext)
                tv.text = message
                tv.setTextColor(Color.BLUE)
                main_message_ll.addView(tv)
            }

            override fun onSentSuccessed(p0: SentBody?) {
                val tv = TextView(mContext)
                tv.text = main_send_message_et.text
                tv.setTextColor(Color.GREEN)
                main_message_ll.addView(tv)
            }

            override fun onNetworkChanged(p0: NetworkInfo?) {
                val tv = TextView(mContext)
                tv.text = "onNetworkChanged"
                tv.setTextColor(Color.RED)
                main_message_ll.addView(tv)
            }

            override fun onConnectionClosed() {
                val tv = TextView(mContext)
                tv.text = "CLOSED"
                tv.setTextColor(Color.RED)
                main_message_ll.addView(tv)
            }

            override fun getEventDispatchOrder(): Int {
                return 0
            }

            override fun onConnectionSuccessed(p0: Boolean) {
                val tv = TextView(mContext)
                tv.text = "CONNECTED"
                tv.setTextColor(Color.GREEN)
                main_message_ll.addView(tv)
            }

        })
//        CIMPushManager.connect(applicationContext, "192.168.1.136", 23456)
    }

    override fun onStart() {
        super.onStart()
//        initView()
    }

    fun initView() {
//        val intent = Intent(mContext, CIMPushService::class.java)


        CIMPushManager.bindAccount(mContext, "android-010101")
        val connedted = CIMPushManager.isConnected(mContext)
        Log.i(TAG, "-----------------------> $connedted")

        main_send_btn.setOnClickListener {
            val s = main_send_message_et.text.toString()
            val sendBody = SentBody()
            sendBody.put("message", s);
            CIMPushManager.sendRequest(mContext, sendBody);
            val isConnected = CIMPushManager.isConnected(mContext)
//            if (!isConnected) {
//                CIMPushManager.connect(applicationContext, "192.168.1.136", 23456)
//                CIMPushManager.bindAccount(mContext, "android-010101")
//            }
            Log.i(TAG, "-----------------------> $isConnected")
        }
    }


    override fun onResume() {
        super.onResume()
        CIMPushManager.resume(mContext)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        CIMPushManager.destroy(applicationContext)
    }
}
