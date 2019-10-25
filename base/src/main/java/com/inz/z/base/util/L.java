package com.inz.z.base.util;

import androidx.annotation.Nullable;

import android.content.Context;
import android.util.Log;

import com.inz.z.base.BuildConfig;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.text.SimpleDateFormat;

/**
 * 日志输出
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/24 10:56.
 */
public class L {

    public static void initL(Context context) {
        // 保存到本地
        FormatStrategy fileStrategy = MyCsvFormatStrategy.newBuilder()
                .dateFormat((SimpleDateFormat) BaseTools.getBaseDateFormat())
                .setContext(context)
                .tag(" - Inz - ")
                .build();
        Logger.addLogAdapter(new DiskLogAdapter(fileStrategy));
        Logger.i("初始化 Logger File 完成 : " + BaseTools.getBaseDateFormat().format(System.currentTimeMillis()));
    }

    public static void initDebug(Context context) {
        if (BuildConfig.DEBUG) {
            FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                    .tag(" - Inz - ")
                    .build();
            Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
                @Override
                public boolean isLoggable(int priority, @Nullable String tag) {
                    return BuildConfig.DEBUG;
                }
            });
            Logger.i("初始化 Logger Logcat 完成 : " + BaseTools.getBaseDateFormat().format(System.currentTimeMillis()));
        } else {
            L.initL(context);
        }
    }

    public static void i(String tag, String message) {
        Logger.t(tag).i(message);
        if (BuildConfig.DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void w(String tag, String message) {
        Logger.t(tag).w(message);
        if (BuildConfig.DEBUG) {
            Log.w(tag, message);
        }
    }

    public static void d(String tag, String message) {
        Logger.t(tag).d(message);
        if (BuildConfig.DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void wtf(String tag, String message) {
        Logger.t(tag).wtf(message);
        if (BuildConfig.DEBUG) {
            Log.w(tag, message);
        }
    }

    public static void e(String tag, String message, Throwable e) {
        Logger.t(tag).e(e, message);
        if (BuildConfig.DEBUG) {
            Log.e(tag, message, e);
        }
    }

}
