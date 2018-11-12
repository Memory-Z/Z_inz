package com.inz.z.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.inz.z.R;
import com.inz.z.util.SPHelper;
import com.inz.z.view.ILoginView;
import com.inz.z.view.fragment.ThirdLoginDialogFragment;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录界面
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create By 2018/8/12 14:54
 */
public class LoginActivity extends AbsBaseActivity implements ILoginView {
    private static final String TAG = "LoginActivity";

    private EditText loginNameEt;
    private EditText loginPasswordEt;
    private DialogFragment thirdDialogFragment;

    @Override
    public void onCreateZ(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        mContext = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showUpload() {

    }


    @Override
    public void initData() {
        // 权限请求
        requestPermission();
        String userInfo = SPHelper.getInstance().getUserLoginInfo();
        if (!"".equals(userInfo)) {
            JSONObject userInfoJson = JSONObject.parseObject(userInfo);
            if (userInfoJson != null) {
                boolean isAutoLogin = userInfoJson.getBooleanValue("isAutoLogin");
                String loginName = userInfoJson.getString("loginName");
                String password = userInfoJson.getString("password");
            }
        } else {
            String userLoginName = SPHelper.getInstance().getUserLoginName();
        }
    }

    @Override
    public void initView() {
        loginNameEt = findViewById(R.id.login_user_name_et);
        loginPasswordEt = findViewById(R.id.login_user_password_et);
        Button loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: loginBtn");
                String loginName = loginNameEt.getText().toString();
                String password = loginPasswordEt.getText().toString();
            }
        });
        TextView loginToRegisterTv = findViewById(R.id.login_to_register_tv);
        loginToRegisterTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: loginToRegisterTv");
                Intent intent = new Intent(mContext, RegisterActivity.class);
                startActivity(intent);
                quitView();
            }
        });
        LinearLayout loginWithThirdLl = findViewById(R.id.login_other_account_ll);
        loginWithThirdLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: loginWithThirdLl");
                thirdDialogFragment = (DialogFragment) getSupportFragmentManager().findFragmentByTag("thirdLoginDialogFragment");
                if (thirdDialogFragment == null) {
                    thirdDialogFragment = new ThirdLoginDialogFragment();
                    thirdDialogFragment.show(getSupportFragmentManager(), "thirdLoginDialogFragment");
                }
            }
        });

    }

    /**
     * 退出视图
     */
    private void quitView() {
        LoginActivity.this.finish();
    }


    private static final int INSTALL_PACKAGE = 10001;
    private static final int INSTALL_PACKAGE_SETTING = 10002;

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
        checkInstallApk();
    }

    /**
     * 检查安装
     */
    private void checkInstallApk() {
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
