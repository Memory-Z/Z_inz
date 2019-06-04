package com.inz.z.other_module.service

import org.apache.mina.core.session.IoSession

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/12 9:44.
 */
interface MinaSocketListener {
    /**
     * 连接失败
     */
    fun connectedFailed()

    /**
     * 消息接收
     */
    fun messageReceived(session: IoSession?, message: Any?)
}