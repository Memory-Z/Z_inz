package com.inz.z.music.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Nullable;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/24 13:18.
 */
public class FileScanService extends Service {
    private static final String TAG = "FileScanService";

    public interface FileScanServiceListener {
        void scanStart();

        void scanning(String fileName, String filePath, String size);

        void scanEnd();

        void scanError(Throwable e);
    }

    private FileScanServiceListener listener;

    public void setListener(FileScanServiceListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (binder == null) {
            binder = new FileScanServiceBinder();
        }
        return binder;
    }

    private FileScanServiceBinder binder;

    public class FileScanServiceBinder extends Binder {

        FileScanService getService() {
            return FileScanService.this;
        }
    }
}
