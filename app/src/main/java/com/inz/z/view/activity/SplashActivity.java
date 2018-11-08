package com.inz.z.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inz.z.R;
import com.inz.z.entity.Constants;
import com.inz.z.util.SPHelper;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 闪屏
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/11/6 10:06.
 */
public class SplashActivity extends AbsBaseActivity {

    private static final int INSTALL_PACKAGE = 10001;
    private static final int INSTALL_PACKAGE_SETTING = 10002;
    /**
     * 倒计时
     */
    private CountDownTimer countDownTimer;

    @Override
    public void onCreateZ(@Nullable Bundle savedInstanceState) {
        // 隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mContext = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void initView() {
        ImageView imageView = findViewById(R.id.splash_iv);
        Glide.with(mContext).load(getResources().getDrawable(R.drawable.adolescent_casual_contemporary_1030895)).into(imageView);
    }

    @Override
    public void initData() {
        requestPermission();
        countDownTimer = new CountDownTimer(5 * 1000 + 500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                boolean isHaveUser = SPHelper.getInstance().isHaveUser();
                Intent intent = new Intent();
                if (isHaveUser) {
                    intent.setClass(mContext, MainActivity.class);
                } else {
                    intent.setClass(mContext, LoginActivity.class);
                }
                startActivity(intent);
            }
        };
        countDownTimer.start();
        // 创建 文件
        Constants.createFile();
    }

    /**
     * 需要申请的权限
     */
    private String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
    };

    /**
     * 权限申请(所有权限)
     */
    private void requestPermission() {
        List<String> list = new ArrayList<>();
        // 权限获取
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                list.add(permission);
            }
        }
        String[] ps = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            ps[i] = list.get(i);
        }
        if (ps.length > 0) {
            ActivityCompat.requestPermissions(this, ps, 1);
        }

        // 检查安装APK
        installApk();
    }

    /**
     * 检查安装
     */
    private void installApk() {
        // 如果大于 Android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 是否支持安装
            boolean canInstallPackage = mContext.getPackageManager().canRequestPackageInstalls();
            if (!canInstallPackage) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGE);
            }
        } else {
            Logger.i("检查更新");
            // 检查更新
//            appUpdate();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INSTALL_PACKAGE_SETTING) {
                // 检查更新
//                appUpdate();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == INSTALL_PACKAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                appUpdate();
            } else {
//                AlertDialog alertDialog = new AlertDialog.Builder(mContext)
//                        .setTitle("提示")
//                        .setMessage("请开启当前程序安装未知应用来源的权限！")
//                        .setNegativeButton("前往", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
//                                startActivityForResult(intent, INSTALL_PACKAGE);
//                            }
//                        })
//                        .create();
//                alertDialog.show();
            }
        }
    }
}
