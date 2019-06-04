package com.inz.z.base.http;

/**
 * 文件下载监听
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/2/21 10:53.
 */
public interface ProgressResponseListener {
    /**
     * 文件读取进度监听
     *
     * @param bytesRead     读取的文件大小
     * @param contentLength 当前文件大小
     * @param done          是否结束
     */
    void onResponseProgress(long bytesRead, long contentLength, boolean done);
}
