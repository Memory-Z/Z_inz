package com.inz.z.other_module.view

import android.annotation.SuppressLint
import android.content.Context
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.ProgressBar
import com.inz.z.other_module.R

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
    }

    override fun onDestroy() {
        super.onDestroy()
        mContext = null
        mWebView?.destroy()
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

        mWebView?.loadUrl("https://cn.bing.com/search?q=viewpager.pageTransformer&qs=n&form=QBRE&sp=-1&pq=viewpager.pagetrans&sc=0-19&sk=&cvid=1165407EA81A4A44B18B9B5FAB23712D")
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

}