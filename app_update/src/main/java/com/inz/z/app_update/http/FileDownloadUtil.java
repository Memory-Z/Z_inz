package com.inz.z.app_update.http;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.SpeedCalculator;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed;
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 文件下载工具
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/15 14:21.
 */
public class FileDownloadUtil {
    private static DownloadTask downloadTask;

    /**
     * 开始下载文件
     *
     * @param downloadUrl 文件下载链接
     * @param filePath    文件保存路径
     * @param listener    文件下载监听
     */
    public static void startDownloadFile(String downloadUrl, String filePath, FileDownloadListener listener) {
        DownloadListener downloadListener = new OkDownloadImpl(listener);
//        int apkIndex = filePath.lastIndexOf(".apk");
//        if (apkIndex != -1) {
//            filePath = filePath.substring(0, apkIndex);
//        }
        File dir = new File(filePath);
        downloadTask = new DownloadTask.Builder(downloadUrl, dir)
                .setAutoCallbackToUIThread(true)
                .setPassIfAlreadyCompleted(false)
                .setMinIntervalMillisCallbackProcess(1000)
                .build();
        downloadTask.execute(downloadListener);
    }

    /**
     * 取消下载
     */
    public static void cancelDownload() {
        if (downloadTask != null) {
            downloadTask.cancel();
        }
    }

    public static void stopDownload() {
        if (downloadTask != null) {
        }
    }

    private static class OkDownloadImpl extends DownloadListener4WithSpeed {
        private FileDownloadListener listener;

        OkDownloadImpl(FileDownloadListener fileDownloadListener) {
            this.listener = fileDownloadListener;
        }

        @Override
        public void taskStart(@NonNull DownloadTask task) {
            if (listener != null) {
                listener.start();
            }
        }

        @Override
        public void connectStart(@NonNull DownloadTask task, int blockIndex, @NonNull Map<String, List<String>> requestHeaderFields) {

        }

        @Override
        public void connectEnd(@NonNull DownloadTask task, int blockIndex, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {

        }

        @Override
        public void infoReady(@NonNull DownloadTask task, @NonNull BreakpointInfo info, boolean fromBreakpoint, @NonNull Listener4SpeedAssistExtend.Listener4SpeedModel model) {
            long total = info.getTotalLength();
            long offset = info.getTotalOffset();
            if (listener != null) {
                listener.taskContent(total);
                listener.progress(offset);
            }
        }

        @Override
        public void progressBlock(@NonNull DownloadTask task, int blockIndex, long currentBlockOffset, @NonNull SpeedCalculator blockSpeed) {

        }

        @Override
        public void progress(@NonNull DownloadTask task, long currentOffset, @NonNull SpeedCalculator taskSpeed) {
            if (listener != null) {
                listener.progress(currentOffset);
                listener.taskSpeed(taskSpeed.instantSpeed());
            }
        }

        @Override
        public void blockEnd(@NonNull DownloadTask task, int blockIndex, BlockInfo info, @NonNull SpeedCalculator blockSpeed) {

        }

        @Override
        public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause, @NonNull SpeedCalculator taskSpeed) {
            if (listener != null) {
                switch (cause) {
                    case COMPLETED:
                        listener.done();
                        break;
                    case CANCELED:
                    case FILE_BUSY:
                    case ERROR:
                    case SAME_TASK_BUSY:
                    case PRE_ALLOCATE_FAILED:
                    default:
                        if (realCause != null) {
                            listener.onError(realCause.getMessage());
                        }
                        break;
                }
            }
        }
    }

}
