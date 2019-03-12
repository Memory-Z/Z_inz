package com.inz.z.app_update.service

import android.annotation.SuppressLint
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

/**
 * 信任所有证书
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/9 16:01.
 */
class TrustAllCertificates : HostnameVerifier, X509TrustManager {
    override fun verify(hostname: String?, session: SSLSession?): Boolean {
        return true
    }

    @SuppressLint("TrustAllX509TrustManager")
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

    }

    @SuppressLint("TrustAllX509TrustManager")
    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
    }

    override fun getAcceptedIssuers(): Array<X509Certificate>? {
        return null
    }

    public fun install() {
        try {
            val trust = TrustAllCertificates()
            val sc: SSLContext = SSLContext.getInstance("SSL")
            sc.init(
                null,
                arrayOf(trust),
                SecureRandom()
            )
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier(trust)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed setting up all thrusting certificate manager.", e)
        } catch (e: KeyManagementException) {
            throw RuntimeException("Failed setting up all thrusting certificate manager.", e)
        }
    }
}