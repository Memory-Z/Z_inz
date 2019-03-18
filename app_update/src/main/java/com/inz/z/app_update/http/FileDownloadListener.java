package com.inz.z.app_update.http;

/**
 * 文件下载监听
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/15 14:22.
 */
public interface FileDownloadListener {

    void start();

    void taskContent(long contentLength);

    void taskSpeed(String speed);

    void progress(long readBytes);

    void onError(String error);

    void done();
}
