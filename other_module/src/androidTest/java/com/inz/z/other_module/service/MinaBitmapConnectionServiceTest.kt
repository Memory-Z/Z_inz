package com.inz.z.other_module.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.test.platform.app.InstrumentationRegistry
import android.util.Log
import android.util.Xml
import org.apache.mina.core.service.IoHandlerAdapter
import org.apache.mina.core.service.IoService
import org.apache.mina.core.service.IoServiceListener
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.codec.ProtocolCodecFilter
import org.apache.mina.filter.codec.textline.LineDelimiter
import org.apache.mina.filter.codec.textline.TextLineCodecFactory
import org.apache.mina.transport.socket.nio.NioSocketConnector
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import java.net.InetSocketAddress
import java.nio.charset.Charset
import java.util.*

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/12 11:54.
 */
class MinaBitmapConnectionServiceTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun onBind() {
        createSocket()
    }

    var mContext: Context? = null

    fun createSocket() {
        mContext = InstrumentationRegistry.getInstrumentation().getContext();
        createMinaSocket()
        try {
            Thread.sleep(900000000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    private inner class MinaSocketServiceConnection : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val minaSocketServiceBind: MinaSocketService.MinaSocketServiceBind =
                service as MinaSocketService.MinaSocketServiceBind
        }
    }

    /**
     * 创建MinaSocket
     */
    private fun createMinaSocket() {
        Thread(Runnable {
            // 创建MinaSocket
            val connector = NioSocketConnector()
            // 设置传输方式
            val chain = connector.filterChain
            val filter = ProtocolCodecFilter(
                TextLineCodecFactory(
                    Charset.forName("UTF-8"),
                    LineDelimiter.WINDOWS,
                    LineDelimiter.WINDOWS
                )
            )
//            // 添加编码过滤器、处理乱码、编码问题
//            chain?.addLast("codeFilter", filter)
//            val codecFilter = ProtocolCodecFilter(
//                TextLineCodecFactory(
//                    Charset.forName("UTF-8"),
//                    LineDelimiter.CRLF,
//                    LineDelimiter.CRLF
//                )
//            )
            // 添加编码过滤器、处理乱码、编码问题
            chain?.addLast("codec", filter)

//            val codecFilter = ProtocolCodecFilter(TextLineCodecFactory(Charset.forName("UTF-8")))
//            chain?.addLast("codec", codecFilter)
            // 消息核心处理器
            connector.handler = MySocketHandler()
            // 连接超时
            connector.connectTimeoutMillis = 30 * 1000
            // 连接超时检查间隔
            connector.connectTimeoutCheckInterval = 10
            if (mySocketListener == null) {
                mySocketListener = MySocketListener()
            }
            connector.addListener(mySocketListener)

            val future = connector.connect(InetSocketAddress("192.168.1.230", 9998))
            // 等待唤醒
            future?.awaitUninterruptibly()
            val ioSession = future.session
            if (ioSession == null) {
                minaSocketListener?.connectedFailed()
                return@Runnable
            }
            ioSession.closeFuture?.awaitUninterruptibly()
            connector.dispose()
        }).start()

    }

    var minaSocketListener: MinaSocketListener? = null


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
            Log.i(MinaSocketService.TAG, "MySocketHandler - messageReceived: message = $message")
            System.out.println("MySocketHandler - messageReceived: message = $message")
//            minaSocketListener?.messageReceived(session, message)
        }

        override fun sessionOpened(session: IoSession?) {
            super.sessionOpened(session)
            Log.i(MinaSocketService.TAG, "MySocketHandler - sessionOpened: ")
            System.out.println("MySocketHandler - sessionOpened: ")
            session?.write("{\"type\":104,\"msg\":\"qwz6_192.168.1.121\"}")
        }

        override fun sessionClosed(session: IoSession?) {
            super.sessionClosed(session)
            Log.i(MinaSocketService.TAG, "MySocketHandler - sessionClosed: ")
            System.out.println("MySocketHandler - sessionClosed: ")
            session?.closeNow()
        }

        override fun messageSent(session: IoSession?, message: Any?) {
            super.messageSent(session, message)
            Log.i(MinaSocketService.TAG, "MySocketHandler - messageSent: message = $message")
            System.out.println("MySocketHandler - messageSent: message = $message")
        }

        override fun inputClosed(session: IoSession?) {
            super.inputClosed(session)
            Log.i(MinaSocketService.TAG, "MySocketHandler - inputClosed: ")
            System.out.println("MySocketHandler - inputClosed: ")
        }

        override fun sessionCreated(session: IoSession?) {
            super.sessionCreated(session)
            Log.i(MinaSocketService.TAG, "MySocketHandler - sessionCreated: ")
            System.out.println("MySocketHandler - sessionCreated: ")
        }

        override fun sessionIdle(session: IoSession?, status: IdleStatus?) {
            super.sessionIdle(session, status)
            Log.i(MinaSocketService.TAG, "sessionIdle: status = $status")
            System.out.println("sessionIdle: status = $status")
        }

        override fun exceptionCaught(session: IoSession?, cause: Throwable?) {
            super.exceptionCaught(session, cause)
            Log.i(MinaSocketService.TAG, "MySocketHandler - exceptionCaught: cause = $cause")
            System.out.println("MySocketHandler - exceptionCaught: cause = $cause")
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
            Log.i(MinaSocketService.TAG, "MySocketListener - sessionDestroyed. ")
            System.out.println("MySocketListener - sessionDestroyed. ")
        }

        /**
         * 服务激活
         */
        override fun serviceActivated(p0: IoService?) {
            Log.i(MinaSocketService.TAG, "MySocketListener - serviceActivated. ")
            System.out.println("MySocketListener - serviceActivated. ")
        }

        /**
         * 服务停用
         */
        override fun serviceDeactivated(p0: IoService?) {
            Log.i(MinaSocketService.TAG, "MySocketListener - serviceDeactivated. ")
            System.out.println("MySocketListener - serviceDeactivated. ")
        }

        /**
         * session 关闭
         */
        override fun sessionClosed(p0: IoSession?) {
            Log.i(MinaSocketService.TAG, "MySocketListener - sessionClosed. ")
            System.out.println("MySocketListener - sessionClosed. ")
        }

        /**
         * session 创建
         */
        override fun sessionCreated(p0: IoSession?) {
            Log.i(MinaSocketService.TAG, "MySocketListener - sessionCreated. ")
            System.out.println("MySocketListener - sessionCreated. ")
        }

        /**
         * 服务空闲
         */
        override fun serviceIdle(p0: IoService?, p1: IdleStatus?) {
            Log.i(MinaSocketService.TAG, "MySocketListener - serviceIdle. IdleStatus = $p1")
            System.out.println("MySocketListener - serviceIdle. IdleStatus = $p1")
        }
    }
}