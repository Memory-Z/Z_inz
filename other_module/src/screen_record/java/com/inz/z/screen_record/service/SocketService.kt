package com.inz.z.screen_record.service

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.os.*
import com.inz.z.base.util.FileUtils
import com.inz.z.base.util.L
import com.inz.z.base.util.NetworkUtil
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/17 13:38.
 */
class SocketService : Service() {
    companion object {
        private const val TAG = "SocketService"
        private const val CLIENT_RECEIVE_MSG = 0x09000
        private const val MESSAGE_START = "--------------------Start--------------------"
        private const val MESSAGE_END = "--------------------End----------------------"
    }

    /**
     * 线程池
     */
    private var executor: ScheduledExecutorService = Executors.newScheduledThreadPool(32)
    /**
     * 服务器Socket
     */
    private var serverSocket: ServerSocket? = null
    /**
     * 连接服务器的客户端Socket集合
     */
    private var clientSocketList: ArrayList<Socket> = ArrayList(32)
    /**
     * 客户端Socket
     */
    private var clientSocket: Socket? = null
    /**
     * 是否为服务器
     */
    private var isServer: Boolean = false
    /**
     * 写入
     */
    private var bufferedWriter: BufferedWriter? = null
    /**
     * 读取
     */
    private var bufferedReader: BufferedReader? = null

    override fun onBind(intent: Intent?): IBinder? {
        if (binder == null) {
            binder = SocketServiceBinder()
        }
        return binder
    }

    private var binder: SocketServiceBinder? = null

    public inner class SocketServiceBinder : Binder() {
        fun getService(): SocketService {
            return this@SocketService
        }
    }

    /**
     * 创建 ServerSocket
     */
    public fun createServerSocket() {
        L.i(TAG, "createServerSocket: ")
        isServer = true
        executor.schedule(CreateServerSocketRunnable(), 100, TimeUnit.MILLISECONDS)
    }

    private var serverSendClientRunnableMap: MutableMap<Socket, Runnable>? = null

    /**
     * 发送服务器消息
     * @param msg 消息内容
     */
    public fun sendServerMessage(msg: String) {
        if (serverSocket != null && !serverSocket!!.isClosed) {
            for (socket: Socket in clientSocketList) {
                if (serverSendClientRunnableMap != null) {
                    val runnable: ServerWriteToClientRunnable =
                        serverSendClientRunnableMap?.get(socket) as ServerWriteToClientRunnable
                    runnable.msg = msg
                }
            }
        }
    }

    /**
     * 创建 ServerSocket 线程
     */
    private inner class CreateServerSocketRunnable : Runnable {

        override fun run() {
            try {
                serverSocket = ServerSocket(0)
                socketServiceListener?.createServerSocket(
                    NetworkUtil.getIpAddress(applicationContext)!!,
                    serverSocket!!.localPort
                )
                var flag = true
                while (flag) {
                    if (serverSocket != null) {
                        if (serverSocket!!.isClosed) {
                            L.i(TAG, "ServerSocket is Closed. ")
                            flag = false
                        } else {
                            val cSocket = serverSocket!!.accept()
                            if (!cSocket.isClosed) {
                                executor.schedule(
                                    ServerWriteToClientRunnable(cSocket),
                                    100,
                                    TimeUnit.MILLISECONDS
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                L.e(TAG, "CreateServerSocketRunnable: ServerSocket(0)", e)
                socketServiceListener?.createServerSocketFailure("创建服务失败", e)
            }
        }
    }

    /**
     * 服务端给全部连接的客户端发送消息
     */
    private inner class ServerWriteToClientRunnable(val socket: Socket) : Runnable {
        var msg = "-------- init -------- ${System.currentTimeMillis()} \r\n"
        private var oldMsg = ""
        private var bufferedWriter: BufferedWriter =
            BufferedWriter(OutputStreamWriter(socket.getOutputStream()))

        override fun run() {
            var flag = true
            while (flag) {
                if (socket.isConnected) {
                    if (msg.isNotEmpty()) {
                        oldMsg = msg
                        msg = ""
                        try {
                            bufferedWriter.newLine()
                            bufferedWriter.write(MESSAGE_START + "\r\n")
                            bufferedWriter.write(oldMsg)
                            bufferedWriter.write(MESSAGE_END + "\r\n")
                            bufferedWriter.newLine()
                            bufferedWriter.flush()
                            oldMsg = ""
                        } catch (e: Exception) {
                            L.e(TAG, "send All msg failure: $socket", e)
                        }
                    }
                    if (!clientSocketList.contains(socket)) {
                        clientSocketList.add(socket)
                    }
                    if (serverSendClientRunnableMap == null) {
                        serverSendClientRunnableMap = HashMap(16)
                    }
                    if (!serverSendClientRunnableMap!!.containsKey(socket)) {
                        serverSendClientRunnableMap!!.put(socket, this)
                    }
                } else {
                    flag = false
                    serverSendClientRunnableMap?.remove(socket)
                    try {
                        bufferedWriter.close()
                    } catch (e: Exception) {
                        L.e(TAG, "bufferedWriter.close()", e)
                    }
                }
            }
            L.i(TAG, "ServerWriteToClientRunnable: to end. $socket")

        }
    }

    private var clientHandler: Handler? = null

    /**
     * 连接 服务器 ServerSocket
     * @param ip 服务器 IP
     * @param port 服务器 端口
     */
    public fun connectServerSocket(ip: String, port: Int) {
        L.i(TAG, "开始连接 ServerSocket: $ip : $port")
        clientHandler = Handler(ClientHandlerCallback())
        executor.schedule(ClientConnectServerSocketRunnable(ip, port), 100, TimeUnit.MILLISECONDS)
    }

    /**
     * 创建 客户端 Socket 连接
     */
    private inner class ClientConnectServerSocketRunnable(val ip: String, val port: Int) :
        Runnable {

        override fun run() {
            try {
                clientSocket = Socket(ip, port)
                if (clientSocket != null && clientSocket!!.isConnected) {
                    executor.schedule(
                        ClientSocketReceiveRunnable(clientSocket!!),
                        100,
                        TimeUnit.MILLISECONDS
                    )
                }
            } catch (e: Exception) {
                socketServiceListener?.connectClientSocketFailure(
                    "连接 ServerSocket 失败： ip = $ip ; port = $port .",
                    e
                )
            }
        }
    }

    /**
     * 客户端 Socket 接收 消息
     */
    private inner class ClientSocketReceiveRunnable(val socket: Socket) : Runnable {
        private var bufferedReader: BufferedReader =
            BufferedReader(InputStreamReader(socket.getInputStream()))
        private var imageStr = ""

        override fun run() {
            var flag = true
            while (flag) {
                if (socket.isConnected) {
                    val msg = bufferedReader.readLine()
                    L.i(TAG, "Receive: ------------------> $msg")
                    if (MESSAGE_START == msg) {
                        imageStr = ""
                    } else if (MESSAGE_END == msg) {
                        executor.execute(ReceiveBitmapStrToBitmapRunnable(imageStr))
                    } else {
                        imageStr += msg + "\r\n"
                    }
                } else {
                    flag = false
                    try {
                        bufferedReader.close()
                    } catch (e: Exception) {
                        L.e(TAG, "bufferedReader.close()", e)
                    }
                }
            }
        }
    }

    /**
     * 客户端 Handler 回调
     */
    private inner class ClientHandlerCallback : Handler.Callback {
        override fun handleMessage(msg: Message?): Boolean {
            val what = msg?.what
            val bundle = msg?.data
            when (what) {
                CLIENT_RECEIVE_MSG -> {
                    val bitmap: Bitmap? = bundle?.getParcelable("imageBitmap")
                    socketServiceListener?.clientReceiveMsg(bitmap!!)
                }
                else -> {

                }
            }

            return true
        }
    }

    private inner class ReceiveBitmapStrToBitmapRunnable(val bitmapStr: String) : Runnable {
        override fun run() {
            var bitmap: Bitmap? = null
            try {
                bitmap = FileUtils.base64ToBitmap(bitmapStr)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (bitmap != null) {
                val message = Message.obtain()
                message.what = CLIENT_RECEIVE_MSG
                val bundle = Bundle()
                bundle.putParcelable("imageBitmap", bitmap)
                message.data = bundle
                clientHandler?.sendMessage(message)
            }

        }
    }

    public var socketServiceListener: SocketServiceListener? = null

    /**
     * 监听
     */
    public interface SocketServiceListener {
        /**
         * 创建服务端Socket
         */
        fun createServerSocket(ip: String, port: Int)

        /**
         * 创建服务端Socket 失败
         */
        fun createServerSocketFailure(msg: String, e: Throwable)

        /**
         * 连接客户端Socket
         */
        fun connectClientSocket()

        /**
         * 连接客户端Socket 失败
         */
        fun connectClientSocketFailure(msg: String, e: Throwable)

        /**
         * 客户端接收到消息
         */
        fun clientReceiveMsg(bitmap: Bitmap)
    }

}