package com.inz.z.base.http;

/**
 * 文件上传进度监听
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/2/21 10:49.
 */
public interface ProgressRequestListener {

    /**
     * 上传进度
     *
     * @param bytesWritten  上传文件大小
     * @param contentLength 整体文件大小
     * @param done          是否结束
     */
    void onRequestProgress(long bytesWritten, long contentLength, boolean done);
}
