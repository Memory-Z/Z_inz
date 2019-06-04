package com.inz.z.other_module.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import org.apache.mina.core.service.IoHandlerAdapter
import org.apache.mina.core.service.IoService
import org.apache.mina.core.service.IoServiceListener
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.codec.ProtocolCodecFilter
import org.apache.mina.filter.codec.textline.LineDelimiter
import org.apache.mina.filter.codec.textline.TextLineCodecFactory
import org.apache.mina.transport.socket.nio.NioSocketConnector
import java.net.InetSocketAddress
import java.nio.charset.Charset
import java.util.*

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/11 13:58.
 */
class MinaSocketService : Service() {

    companion object {
        const val TAG = "MinaSocketService"
    }

    private var minaSocketServiceBind: MinaSocketServiceBind? = null
    private val socketHostName = "192.168.1.230"
    private val socketPort = 9998

    override fun onBind(intent: Intent?): IBinder? {
        if (minaSocketServiceBind == null) {
            minaSocketServiceBind = MinaSocketServiceBind()
        }
        createMinaSocket()
        return minaSocketServiceBind
    }

    public inner class MinaSocketServiceBind : Binder() {
        fun getService(): MinaSocketService {
            return this@MinaSocketService
        }
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        if (connector == null) {
            createMinaSocket()
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        stopSelf()
        connector?.removeListener(mySocketListener)
        connector?.filterChain?.clear()
        ioSession?.closeNow()
        ioSession?.closeOnFlush()
        return super.onUnbind(intent)
    }

    private var connector: NioSocketConnector? = null
    private var ioSession: IoSession? = null

    /**
     * 创建MinaSocket
     */
    private fun createMinaSocket() {
        Thread(Runnable {
            // 创建MinaSocket
            connector = NioSocketConnector()
            // 设置传输方式
            val chain = connector?.filterChain
            val filter = ProtocolCodecFilter(
                TextLineCodecFactory(
                    Charset.forName("UTF-8"),
                    LineDelimiter.WINDOWS,
                    LineDelimiter.WINDOWS
                )

            )
            // 添加编码过滤器、处理乱码、编码问题
            chain?.addLast("codec", filter)
            // 设置消息处理
            if (myMinaSocketHandler == null) {
                myMinaSocketHandler = MySocketHandler()
            }
            // 消息核心处理器
            connector?.handler = myMinaSocketHandler
            // 连接超时
            connector?.connectTimeoutMillis = 30 * 1000
            // 连接超时检查间隔
            connector?.connectTimeoutCheckInterval = 30
            if (mySocketListener == null) {
                mySocketListener = MySocketListener()
            }
            connector?.addListener(mySocketListener)

            val future = connector?.connect(InetSocketAddress(socketHostName, socketPort))
            // 等待唤醒
            val cf = future?.awaitUninterruptibly()
            ioSession = cf?.session
            if (ioSession == null) {
                minaSocketListener?.connectedFailed()
                return@Runnable
            }
            ioSession?.closeFuture?.awaitUninterruptibly()
            connector?.dispose()
            Timer().schedule(CheckTimerTask(), 10 * 1000, 10 * 1000)
        }).start()

    }

    var minaSocketListener: MinaSocketListener? = null


    private inner class CheckTimerTask : TimerTask() {
        override fun run() {
            if (ioSession != null) {
                ioSession?.write("{\"type\":104,\"msg\":\"qwz5_192.168.1.121\"}")
            } else {
                createMinaSocket()
            }
        }
    }

    private var myMinaSocketHandler: MySocketHandler? = null

    /**
     * MinaSocket 消息处理
     */
    private inner class MySocketHandler : IoHandlerAdapter() {
        /**
         * 接收消息
         */
        override fun messageReceived(session: IoSession?, message: Any?) {
            super.messageReceived(session, message)
            Log.i(TAG, "MySocketHandler - messageReceived: message = $message")
            minaSocketListener?.messageReceived(session, message)
        }

        override fun sessionOpened(session: IoSession?) {
            super.sessionOpened(session)
            Log.i(TAG, "MySocketHandler - sessionOpened: ")
            try {
                session?.write("{\"type\":104,\"msg\":\"qwe4_192.168.1.233\"}")
            } catch (e: Exception) {
                e.printStackTrace();
            }
        }

        override fun sessionClosed(session: IoSession?) {
            super.sessionClosed(session)
            Log.i(TAG, "MySocketHandler - sessionClosed: ")
            session?.closeNow()
        }

        override fun messageSent(session: IoSession?, message: Any?) {
            super.messageSent(session, message)
            Log.i(TAG, "MySocketHandler - messageSent: message = $message")
        }

        override fun inputClosed(session: IoSession?) {
            super.inputClosed(session)
            Log.i(TAG, "MySocketHandler - inputClosed: ")
        }

        override fun sessionCreated(session: IoSession?) {
            super.sessionCreated(session)
            Log.i(TAG, "MySocketHandler - sessionCreated: ")
        }

        override fun sessionIdle(session: IoSession?, status: IdleStatus?) {
            super.sessionIdle(session, status)
            Log.i(TAG, "sessionIdle: status = $status")
        }

        override fun exceptionCaught(session: IoSession?, cause: Throwable?) {
            super.exceptionCaught(session, cause)
            Log.i(TAG, "MySocketHandler - exceptionCaught: cause = $cause")
        }
    }

    private var mySocketListener: MySocketListener? = null

    /**
     * Service 监听
     */
    private inner class MySocketListener : IoServiceListener {

        /**
         * session 销毁
         */
        override fun sessionDestroyed(p0: IoSession?) {
            Log.i(TAG, "MySocketListener - sessionDestroyed. ")
        }

        /**
         * 服务激活
         */
        override fun serviceActivated(p0: IoService?) {
            Log.i(TAG, "MySocketListener - serviceActivated. ")
        }

        /**
         * 服务停用
         */
        override fun serviceDeactivated(p0: IoService?) {
            Log.i(TAG, "MySocketListener - serviceDeactivated. ")
        }

        /**
         * session 关闭
         */
        override fun sessionClosed(p0: IoSession?) {
            Log.i(TAG, "MySocketListener - sessionClosed. ")
        }

        /**
         * session 创建
         */
        override fun sessionCreated(p0: IoSession?) {
            Log.i(TAG, "MySocketListener - sessionCreated. ")
        }

        /**
         * 服务空闲
         */
        override fun serviceIdle(p0: IoService?, p1: IdleStatus?) {
            Log.i(TAG, "MySocketListener - serviceIdle. IdleStatus = $p1")
        }
    }

}