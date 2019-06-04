package com.inz.z.music.base;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/04 10:06.
 */
public abstract class AbsBaseActivity extends AppCompatActivity {
    protected Context mContext;

    protected abstract void initWindow();

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
        mContext = this;
        initView();
        initData();
        // 初始化请求权限
        requestPermission(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext = null;
    }

    /**
     * 需要请求的权限
     *
     * @return 权限数组
     */
    @Nullable
    protected abstract String[] needRequestPermission();

    private static final int ABS_REQUEST_CODE = 0x00011100;

    /**
     * 请求权限
     *
     * @param accordPermissions 主动请求权限
     */
    protected void requestPermission(@Nullable String[] accordPermissions) {
        String[] permissions = accordPermissions == null ? needRequestPermission() : accordPermissions;
        if (permissions == null) {
            return;
        }
        List<String> needRequestList = new ArrayList<>();
        for (String permission : permissions) {
            int havePermission = ContextCompat.checkSelfPermission(mContext, permission);
            if (havePermission == PackageManager.PERMISSION_DENIED) {
                // 未获取权限，去请求
                needRequestList.add(permission);
            }
        }
        String[] needRequestArray = new String[needRequestList.size()];
        needRequestArray = needRequestList.toArray(needRequestArray);
        if (needRequestArray.length > 0) {
            ActivityCompat.requestPermissions(this, needRequestArray, ABS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ABS_REQUEST_CODE) {
            // 是否全部同意
            boolean isAllGranted = true;
            int i = 0;
            List<String> deniedPermissionList = new ArrayList<>();
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    isAllGranted = false;
                    // 用户拒绝权限
                    String permission = permissions[i];
                    deniedPermissionList.add(permission);
                }
                i++;
            }
            if (isAllGranted) {
                dealAllGrantedPermission();
            } else {
                String[] strings = new String[deniedPermissionList.size()];
                dealDeniedPermission(deniedPermissionList.toArray(strings));
            }
        }
    }

    /**
     * 处理用户拒绝的权限
     *
     * @param permissions 被拒绝的权限
     */
    protected void dealDeniedPermission(String[] permissions) {

    }

    /**
     * 处理全部同意权限处理
     */
    protected void dealAllGrantedPermission() {

    }
}
