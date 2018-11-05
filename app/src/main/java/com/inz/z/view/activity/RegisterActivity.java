package com.inz.z.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.inz.z.R;
import com.inz.z.view.IRegisterView;
import com.inz.z.view.fragment.CheckEmailDialogFragment;
import com.inz.z.view.fragment.ThirdLoginDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/23 19:34.
 */
public class RegisterActivity extends AbsBaseActivity implements IRegisterView {
    private static final String TAG = "RegisterActivity";
    private static final int INSTALL_PACKAGE = 100001;
    private static final int INSTALL_PACKAGE_SETTING = 100002;

    /**
     * 第三方登录按钮
     */
    private LinearLayout thirdLoginLl;
    private DialogFragment thirdDialogFragment;
    private Button registerBtn;
    private DialogFragment checkEmailDialogFragment;
    private RelativeLayout toLoginRl;

    @Override
    public void onCreateView(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        mContext = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        thirdLoginLl = findViewById(R.id.register_other_account_ll);
        thirdLoginLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdDialogFragment = (DialogFragment) getSupportFragmentManager().findFragmentByTag("thirdLoginDialogFragment");
                if (thirdDialogFragment == null) {
                    thirdDialogFragment = new ThirdLoginDialogFragment();
                    thirdDialogFragment.show(getSupportFragmentManager(), "thirdLoginDialogFragment");
                }
            }
        });
        registerBtn = findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailDialogFragment = (DialogFragment) getSupportFragmentManager().findFragmentByTag("checkEmailDialogFragment");
                if (checkEmailDialogFragment == null) {
                    checkEmailDialogFragment = new CheckEmailDialogFragment();
                    checkEmailDialogFragment.show(getSupportFragmentManager(), "checkEmailDialogFragment");
                }
            }
        });
        toLoginRl = findViewById(R.id.register_to_login_rl);
        toLoginRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 需要申请的权限
     */
    private String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
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
        // 如果大于 Android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 是否支持安装
            boolean canInstallPackage = mContext.getPackageManager().canRequestPackageInstalls();
            if (!canInstallPackage) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGE);
            }
        } else {
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
                AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                        .setTitle("提示")
                        .setMessage("请开启当前程序安装未知应用来源的权限！")
                        .setNegativeButton("前往", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                                startActivityForResult(intent, INSTALL_PACKAGE);
                            }
                        })
                        .create();
                alertDialog.show();
            }
        }
    }
}
