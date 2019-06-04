package com.inz.z.other_module.view

import android.annotation.SuppressLint
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.ProgressBar
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.JSONPObject
import com.inz.z.other_module.R
import com.inz.z.other_module.service.MinaSocketListener
import com.inz.z.other_module.service.MinaSocketService
import org.apache.mina.core.session.IoSession
import java.io.Serializable

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/11 15:58.
 */
class MinaActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MinaActivity"
    }

    private var mContext: Context? = null
    private var mWebView: WebView? = null
    private var mProgressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_mina)
        mContext = this
        initView()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        mContext = null
        mWebView?.destroy()
        try {
            unbindService(minaSocketConnection!!)
            minaSocketConnection = null
        } catch (e: Exception) {
            Log.e(TAG, "close Service Error: ", e)
        }
        minaSocketService = null
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        mProgressBar = findViewById(R.id.activity_mina_pb)
        mProgressBar?.max = 100
        mWebView = findViewById(R.id.activity_mina_wbv)
        val settings = mWebView?.settings
        settings?.javaScriptEnabled = true
        settings?.cacheMode = WebSettings.LOAD_NO_CACHE
        settings?.displayZoomControls = false
        settings?.blockNetworkLoads = false
        settings?.blockNetworkImage = false
        settings?.loadWithOverviewMode = true

        mWebView?.loadUrl("https://bbs.csdn.net/topics/330144150")
        mWebView?.webChromeClient = MyWebChromeClient()
        mWebView?.webViewClient = MyWebClient()
        mWebView?.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                val canBack = mWebView?.canGoBack()
                if (keyCode == KeyEvent.KEYCODE_BACK && canBack!!) {
                    mWebView?.goBack()
                    return@setOnKeyListener true
                }
            }
            return@setOnKeyListener false
        }

    }

    private fun initData() {
        val serviceIntent = Intent(mContext, MinaSocketService::class.java)
        if (minaSocketConnection == null) {
            minaSocketConnection = MinaSocketServiceConnection()
        }
        bindService(serviceIntent, minaSocketConnection!!, Service.BIND_AUTO_CREATE)
    }

    private inner class MyWebChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if (newProgress >= 100) {
                mProgressBar?.visibility = View.GONE
            } else {
                mProgressBar?.visibility = View.VISIBLE
                mProgressBar?.progress = newProgress
            }

        }
    }

    private inner class MyWebClient : WebViewClient() {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val urlStr = request?.url?.toString()
            Log.i(TAG, "url = $urlStr")
            mWebView?.loadUrl(urlStr)
            return true
        }

        override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
            return super.shouldOverrideKeyEvent(view, event)
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
        }

        override fun onReceivedHttpError(
            view: WebView?,
            request: WebResourceRequest?,
            errorResponse: WebResourceResponse?
        ) {
            super.onReceivedHttpError(view, request, errorResponse)
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            super.onReceivedSslError(view, handler, error)
        }


    }

    private var minaSocketService: MinaSocketService? = null

    private var minaSocketConnection: ServiceConnection? = null

    private inner class MinaSocketServiceConnection : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            minaSocketService = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val minaSocketServiceBind: MinaSocketService.MinaSocketServiceBind =
                service as MinaSocketService.MinaSocketServiceBind
            minaSocketService = minaSocketServiceBind.getService()
            minaSocketService?.minaSocketListener = MinaSocketListenerImpl()
        }
    }

    private inner class MinaSocketListenerImpl : MinaSocketListener {

        override fun connectedFailed() {
        }

        override fun messageReceived(session: IoSession?, message: Any?) {
            Log.i(TAG, "---------------- " + message)
            val obj = JSONObject.parseObject(message as String)
            val url = obj.getString("msg")
            runOnUiThread {
                mWebView?.loadUrl(url)
            }

        }
    }

    private inner class InnerEntity : Serializable {
        var type: Int = 0
        var msg = ""
    }
}