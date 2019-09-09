package com.inz.z.music.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.inz.z.base.util.L;
import com.inz.z.music.R;
import com.inz.z.music.base.AbsBaseActivity;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/18 13:56.
 */
public class SettingActivity extends AbsBaseActivity {
    private static final String TAG = "SettingActivity";

    private Window window;
    private Switch notificationSwitch;

    @Override
    protected void initWindow() {
        window = getWindow();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.setting_activity;
    }

    @Override
    protected void initView() {
        window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.musicWhite));
        notificationSwitch = findViewById(R.id.setting_notification_nav_right_switch);
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (notificationSwitch.isPressed()) {
                    L.i(TAG, "用户点击");
                }
            }
        });
    }

    @Override
    protected void initData() {
        requestNotificationPermission();
    }

    @Nullable
    @Override
    protected String[] needRequestPermission() {
        return new String[0];
    }

    /**
     * 请求通知权限
     */
    private void requestNotificationPermission() {
        boolean haveNotification = NotificationManagerCompat.from(mContext).areNotificationsEnabled();
        notificationSwitch.setChecked(haveNotification);
        if (!haveNotification) {
            // 未获取通知状态
            settingNotificationPermission();
        }
    }

    private int requestNotificationCode = 0x100;

    /**
     * 设置通知栏权限
     */
    @SuppressLint("ObsoleteSdkInt")
    private void settingNotificationPermission() {
        Intent intent = new Intent();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + getPackageName()));
        } else {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        }
        startActivityForResult(intent, requestNotificationCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestNotificationCode) {
            L.i(TAG, "setNotification = " + resultCode);
        }
    }
}
