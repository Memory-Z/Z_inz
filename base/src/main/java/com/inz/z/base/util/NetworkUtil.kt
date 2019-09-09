package com.inz.z.base.util

import android.app.usage.NetworkStats
import android.content.Context
import android.net.*
import android.net.wifi.WifiManager
import androidx.annotation.NonNull
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/15 11:40.
 */
object NetworkUtil {
    /**
     * 获取当前网络IP 地址 ，就方法
     */
    fun getIpAddress(context: Context): String? {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info: NetworkInfo = manager.activeNetworkInfo
        if (info.isConnected) {
            if (info.type == ConnectivityManager.TYPE_MOBILE) {
                try {
                    val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
                    while (en.hasMoreElements()) {
                        val networkInter = en.nextElement()
                        val enumIpAddress: Enumeration<InetAddress> = networkInter.inetAddresses
                        while (enumIpAddress.hasMoreElements()) {
                            val address = enumIpAddress.nextElement()
                            if (!address.isLoopbackAddress && address is Inet4Address) {
                                return address.hostAddress
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (info.type == ConnectivityManager.TYPE_WIFI) {
                val wifiManager: WifiManager =
                    context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wifiInfo = wifiManager.connectionInfo
                return intIP2StringIP(wifiInfo.ipAddress)
            }
        }
        return null
    }

    /**
     * Int 类型IP 转换为 String 类型
     */
    public fun intIP2StringIP(ip: Int): String {
        return ip.and(0xFF).toString() + "." +
                ip.shr(8).and(0xFF) + "." +
                ip.shr(16).and(0xFF) + "." +
                ip.shr(24).and(0xFF)
    }


    /**
     * 注册网络监听
     */
    public fun registerNetworkCallback(context: Context, @NonNull listener: NetworkStateListener) {
        val manager: ConnectivityManager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val request = NetworkRequest.Builder().build()
        manager.registerNetworkCallback(request, NetworkCallbackExt(listener))
    }

    /**
     * 网络状态回调
     */
    private class NetworkCallbackExt : ConnectivityManager.NetworkCallback {

        var listener: NetworkStateListener? = null

        constructor() : super()
        constructor(listener: NetworkStateListener?) : super() {
            this.listener = listener
        }

        override fun onCapabilitiesChanged(
            network: Network?,
            networkCapabilities: NetworkCapabilities?
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
        }

        override fun onLost(network: Network?) {
            super.onLost(network)
        }

        override fun onLinkPropertiesChanged(network: Network?, linkProperties: LinkProperties?) {
            super.onLinkPropertiesChanged(network, linkProperties)
        }

        override fun onUnavailable() {
            super.onUnavailable()
        }

        override fun onLosing(network: Network?, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
        }

        override fun onAvailable(network: Network?) {
            super.onAvailable(network)
        }
    }

    public interface NetworkStateListener {

    }
}