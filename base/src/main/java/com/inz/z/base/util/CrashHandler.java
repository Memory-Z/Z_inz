package com.inz.z.base.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 系统崩溃日志 异常处理程序
 * 参考 ： <a href="https://www.jianshu.com/p/16b0b2fd8c0e">https://www.jianshu.com/p/16b0b2fd8c0e</a>
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/25 9:15.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final String LOG_PREFIX = "inz";
    /**
     * 时间格式: yyyy-MM-dd HH:mm:ss
     */
    private DateFormat dateFormat = BaseTools.getBaseDateFormat();
    private Calendar calendar = Calendar.getInstance(Locale.CHINA);
    /**
     * 系统 默认处理类
     */
    private static Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    /**
     * 时间字符串
     */
    private String dateStr = dateFormat.format(calendar.getTime());
    /**
     * 日志信息
     */
    private Map<String, Object> logInfoMap = new HashMap<>();
    /**
     * 上下文
     */
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    @SuppressLint("StaticFieldLeak")
    private static CrashHandler crashHandler;

    /**
     * 初始化
     *
     * @param context 上下文
     */
    private static void init(Context context) {
        mContext = context;
        // 获取 系统的 异常处理程序
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置 异常处理程序 未 当前 程序
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    /**
     * 获取实例 ： 单例模式
     * <p>
     * //     * @return 自定义异常处理
     */
    public static void instance(Context context) {
        if (crashHandler == null) {
            crashHandler = new CrashHandler();
        }
        init(context);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // 设置崩溃状态为 true
        SPHelper.getInstance().setCrashState(true);
        if (!handlerException(e) && uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(t, e);
        } else {
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                Log.e(TAG, "uncaughtException: Sleep; ", e);
            }
            Process.killProcess(Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义 异常处理 方法
     *
     * @param e 异常信息
     * @return 是否处理成功
     */
    private boolean handlerException(Throwable e) {
        boolean flag = false;
        if (e != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 准备消息队列
                    Looper.prepare();
                    Toast.makeText(mContext, "很抱歉，程序出现异常，即将退出", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }).start();
            // 收集 设备参数 信息
            collectDriveInfo(mContext);
            // 保存 日志信息
            String filePath = saveCrashInfo2File(e, LOG_PREFIX);
            if (!"".equals(filePath)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 收集设备参数信息
     *
     * @param context 上下文
     */
    private void collectDriveInfo(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                String versionName = packageInfo.versionName == null ? "null" : packageInfo.versionName;
                String versionCode;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    versionCode = packageInfo.getLongVersionCode() + "";
                } else {
                    versionCode = packageInfo.versionCode + "";
                }
                logInfoMap.put("versionName", versionName);
                logInfoMap.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "collectDriveInfo: an error occured when collect package info ", e);
        }
        Field[] fields = Build.class.getFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                logInfoMap.put(field.getName(), field.get(null).toString());
                Log.i(TAG, "collectDriveInfo: Name: " + field.getName() + " ; Str: " + field.get(null).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                Log.e(TAG, "collectDriveInfo: ", e);
            }
        }
    }

    /**
     * 保存 异常 日志 到本地文件
     *
     * @param e      异常信息
     * @param prefix 文件前缀
     * @return 文件地址
     */
    @SuppressWarnings("SameParameterValue")
    private String saveCrashInfo2File(Throwable e, String prefix) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : logInfoMap.entrySet()) {
            String key = entry.getKey();
            String value = (String) entry.getValue();
            sb.append(key).append(" = ").append(value).append(" ,\r\n");
        }
        // 添加崩溃日志写入时间：
        sb.append("------------ CRASH_WRITER_TIME --------------------- \n")
                .append(BaseTools.getBaseDateFormat().format(System.currentTimeMillis()))
                .append("\n")
                .append("------------ CRASH_WRITER_TIME --------------------- \n")
                .append("\n\n");
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        Throwable cause = e.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        // 保存到 本地
        String filePath = saveLogToFile(sb.toString(), prefix);
        // 上传日志
        uploadLogToService(sb.toString());
        return filePath;
    }

    /**
     * 保存 日志 至文件
     *
     * @param content 内容
     * @param prefix  前缀
     */
    private String saveLogToFile(String content, String prefix) {
        L.i(TAG, "--------------- " + content);
        String fileName = prefix + "-" + dateStr + ".log";
        String filePath = FileUtils.getCacheCrashLogPath(mContext) + File.separator + fileName;
        File dir = new File(filePath);
        boolean isMkdirs = true;
        if (!dir.getParentFile().exists()) {
            isMkdirs = dir.getParentFile().mkdirs();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            isMkdirs = true;
        }
        return isMkdirs ? filePath : "";
    }

    /**
     * 上传日志 至 服务器
     *
     * @param content 上下文
     */
    private void uploadLogToService(String content) {
        Log.i(TAG, "uploadLogToService: " + content);
    }
}
