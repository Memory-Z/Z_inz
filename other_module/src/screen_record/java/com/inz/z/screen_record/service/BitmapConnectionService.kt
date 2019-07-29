package com.inz.z.screen_record.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import com.inz.z.base.util.L
import com.inz.z.base.util.NetworkUtil
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.Charset
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


/**
 * Socket 服务 传输/接收 图片
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/15 14:27.
 */
class BitmapConnectionService : Service() {

    companion object {
        private val TAG = "BitmapConnectionService"

        /**
         * 连接类型
         */
        public enum class ConnectType {
            HTTP,
            SOCKET,
            BLUETOOTH

        }
    }

    private var binder: SocketServiceBinder? = null
    /**
     * 默认连接类型为 Http 网络连接
     */
    private var connectType: ConnectType = Companion.ConnectType.HTTP
    // 连接Ip 和 端口
    private lateinit var connectIP: String
    private var connectPort = 7890
    private var executors = Executors.newScheduledThreadPool(32)
    private lateinit var mHandler: Handler


    inner class SocketServiceBinder : Binder() {
        internal val service: BitmapConnectionService
            get() = this@BitmapConnectionService

        fun getService(): BitmapConnectionService {
            return this@BitmapConnectionService
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        if (binder == null) {
            binder = SocketServiceBinder()
        }
        initData()
        return binder
    }

    private fun initData() {
        mHandler = Handler()
        connectIP = NetworkUtil.getIpAddress(applicationContext)!!

    }

    /* =================================== java Socket =================================== */
    private var mSocket: Socket? = null
    private var mServerSocket: ServerSocket? = null
    private var mAcceptClientArray: ArrayList<Socket>? = null
    /**
     * 创建Socket 连接
     */
    public fun createServerSocket() {

        mAcceptClientArray = ArrayList()
        executors.schedule(CreateServerSocketRunnable(), 100, TimeUnit.MILLISECONDS)
//        val service = ServiceSocketDemo()
//        service.createServer()

    }

    /**
     * 创建 服务端 Socket 线程
     */
    private inner class CreateServerSocketRunnable : Runnable {
        override fun run() {
            try {
                mServerSocket = ServerSocket(0)
                val s = mServerSocket?.inetAddress?.canonicalHostName
                serviceSocketListener?.getAddress(
                    connectIP,
                    mServerSocket?.localPort.toString()
                )
                L.i(TAG, "CreateServerSocketRunnable: run: $mServerSocket - $s - ")
                while (true) {
                    try {
                        val socket: Socket = mServerSocket!!.accept()
                        L.i(TAG, "CreateServerSocketRunnable: run: $socket")
                        mAcceptClientArray?.add(socket)
                        executors.execute(ServerClientSocketRunnable(socket))
                    } catch (e1: Exception) {
                        L.e(TAG, "mServerSocket!!.accept() --- ", e1)
                    }
                }
            } catch (e: Exception) {
                L.e(TAG, "ServerSocket-Error", e)
            }
        }
    }

    /**
     * 服务端 Socket 读写线程
     */
    private inner class ServerClientSocketRunnable(val mSocket: Socket?) : Runnable {
        //        private var mBufferedReader: BufferedReader
        private lateinit var mBufferedWriter: BufferedWriter
        private var msg: String? = "init - "
        private var num = 0

        override fun run() {
            L.i(TAG, " ---------------- $mSocket")
            if (mSocket != null) {
                mBufferedWriter = BufferedWriter(OutputStreamWriter(mSocket.getOutputStream()))
            }
            while (true) {
                mBufferedWriter.write("$msg - $num  \r\n")
                mBufferedWriter.flush()
                num += 1
            }
        }
    }

    /* --------------- */
    /**
     * 创建 客户端 Socket 线程
     */
    public fun createClientSocket(socketIP: String, socketPort: Int) {
        connectIP = socketIP
        connectPort = socketPort
        executors.execute(ClientSocketRunnable(connectIP, connectPort))
    }

    private inner class ClientSocketRunnable(private val ip: String, private var port: Int) :
        Runnable {

        private lateinit var mBufferedReader: BufferedReader
//        private lateinit var mPrintWriter: PrintWriter

        override fun run() {
            try {
                mSocket = Socket(ip, port)
                var flag = true
                while (flag) {
                    if (!mSocket!!.isClosed) {
                        mBufferedReader = BufferedReader(
                            InputStreamReader(
                                mSocket!!.getInputStream(),
                                Charset.forName("UTF-8")
                            )
                        )
                        try {
                            var msg = mBufferedReader.readLine()
                            while (msg != null) {
                                L.i(TAG, "Client: read = $msg")
                                msg = mBufferedReader.readLine()
                            }
                        } catch (e2: Exception) {
                            L.e(TAG, "mBufferedReader.readLine() -- ", e2)
                        }
                    } else {
                        flag = false
                    }
                }
            } catch (e: Exception) {
                L.e(TAG, "Socket: $ip : $port", e)
            }
        }
    }

    public interface ServiceSocketListener {
        fun getAddress(ip: String, port: String)
    }

    var serviceSocketListener: ServiceSocketListener? = null

/* =================================== java Socket =================================== */

    private fun createWebSocket() {

    }

    public fun sendBitmapString(bitmapStr: String) {

    }
}
