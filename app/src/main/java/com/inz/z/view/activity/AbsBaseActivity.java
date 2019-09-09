package com.inz.z.view.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;

import com.inz.z.app_update.UpdateWrapper;
import com.inz.z.app_update.bean.VersionBean;
import com.inz.z.app_update.service.CheckUpdateThread;
import com.inz.z.base.IBaseView;
import com.inz.z.util.AppBaseTools;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础 Activity 类
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/23 16:38.
 */
public abstract class AbsBaseActivity extends AppCompatActivity implements IBaseView {

    /**
     * 上下文
     */
    protected Context mContext;

    private Dialog loadDialog;

    /**
     * 初始化窗口
     */
    protected abstract void initWindow();

    /**
     * 获取内容布局ID
     *
     * @return 布局ID
     */
    @LayoutRes
    protected abstract int getContentViewId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(getContentViewId());
        mContext = this;
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    /**
     * 初始化界面
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /* / -- IBaseView - Start -- / */
    @Override
    public void showLoading() {
        if (loadDialog == null) {
            loadDialog = AppBaseTools.loadDialog(mContext);
        }
        if (!loadDialog.isShowing()) {
            loadDialog.show();
        }
        Logger.i(mContext.getPackageName() + "; showLoading.");
    }

    @Override
    public void hideLoading() {
        if (loadDialog != null) {
            loadDialog.dismiss();
            loadDialog = null;
        }
        Logger.i(mContext.getPackageName() + "; hideLoading.");
    }

    @Override
    public void showToast(String msg) {
        AppBaseTools.showShortCenterToast(mContext, msg);
        Logger.i(mContext.getPackageName() + "; showToast: " + msg);
    }

    @Override
    public void showError(Throwable e) {
        AppBaseTools.showShortCenterToast(mContext, e.getMessage());
        Logger.e(mContext.getPackageName() + "; showError: " + e.getMessage());
    }

    @Override
    public Context getContext() {
        return mContext;
    }
    /* / -- IBaseView - End -- / */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Logger.i("onKeyDown : " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (loadDialog != null && loadDialog.isShowing()) {
                loadDialog.dismiss();
                return true;
            }
        }
        return myOnKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    /**
     * 自定义按钮 点击
     *
     * @param keyCode 点击按键
     * @param event   事件
     * @return 是否拦截
     */
    public abstract boolean myOnKeyDown(int keyCode, KeyEvent event);

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
    protected void requestPermission() {
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

    }

    /**
     * 更新
     */
    protected void checkUpdate() {
        UpdateWrapper updateWrapper = new UpdateWrapper.Builder(this)
                .setIsPost(true)
                .setIsShowToast(false)
                .setNotificationIcon(mContext.getApplicationInfo().icon)
                .setUrl("")
                .setCallback(new CheckUpdateThread.CallBack() {
                    @Override
                    public void callBack(@org.jetbrains.annotations.Nullable VersionBean versionBean, boolean hasNewVersion) {

                    }
                })
                .build();
        updateWrapper.start();
    }
}
