package com.inz.z.app_update.utils;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 通知栏工具
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/12/18 9:29.
 */
public class NotificationUtils {

    public static final int REQUEST_SETTING_NOTIFICATION = 10000;

    private static String channelId = "link_court_state";
    private static String channelName = "调解状态";
    private static int pendingIntentRequestCode = 100;

    private NotificationUtils() {
        super();
    }

    /**
     * 是否开启通知栏权限
     *
     * @param context 上下文
     * @return 是否开启
     */
    public static boolean isNotificationEnabled(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager == null) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //8.0手机以上
            if (manager.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                return false;
            }
        }
        // SDK 24 及以上 判断是否 开启通知权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return NotificationManagerCompat.from(context).areNotificationsEnabled();
        }

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;

        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 跳转至 允许通知  设置页面
     *
     * @param activity A
     */
    public static void gotoNotificationSetting(Activity activity) {
        ApplicationInfo appInfo = activity.getApplicationInfo();
        String pkg = activity.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, pkg);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid);
                activity.startActivityForResult(intent, REQUEST_SETTING_NOTIFICATION);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                intent.putExtra("app_package", pkg);
                intent.putExtra("app_uid", uid);
                activity.startActivityForResult(intent, REQUEST_SETTING_NOTIFICATION);
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                activity.startActivityForResult(intent, REQUEST_SETTING_NOTIFICATION);
            } else {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                activity.startActivityForResult(intent, REQUEST_SETTING_NOTIFICATION);
            }
        } catch (Exception e) {
            try {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                activity.startActivityForResult(intent, REQUEST_SETTING_NOTIFICATION);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }


    private static NotificationManager notificationManager;

    public static NotificationManager getNotificationManager(Context context) {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    //    private static int notificationId = 0;
//
//    /**
//     * 创建通知
//     *
//     * @param context       上下文
//     * @param contentTitle  内容标题
//     * @param contentStr    内容
//     * @param pendingIntent 上下文
//     * @param type          类型：1、弹窗；2、不弹窗
//     */
//    private static void createNotification(Context context, String contentTitle, String contentStr, PendingIntent pendingIntent, int type) {
//        if (notificationManager == null) {
//            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        }
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
//        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
//        builder.setChannelId(channelId);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
//            notificationManager.createNotificationChannel(channel);
//        }
//        // 设置 ticker
//        builder.setTicker("调解信息通知");
//        builder.setContentTitle(contentTitle);
//        builder.setContentText(contentStr);
//        // 标识 为正在进行的任务
//        builder.setOngoing(true);
//        // 设置优先级
//        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
//        // 设置为 灯光提示
//        builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);
//        if (type == 1) {
//            builder.setFullScreenIntent(pendingIntent, true);
//        }
//        builder.setContentIntent(pendingIntent);
//        Notification notification = builder.build();
//        notification.flags = Notification.FLAG_NO_CLEAR;
//        notificationManager.notify(channelId, notificationId, notification);
//    }
//
//    /**
//     * 取消通知
//     *
//     * @param context 上下文
//     */
//    public static void cancelNotify(Context context) {
//        if (notificationManager == null) {
//            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        }
//        if (notificationManager != null) {
//            notificationManager.cancel(channelId, notificationId);
//        }
//    }


}
