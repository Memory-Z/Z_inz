package com.inz.z.music.view.activity;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Window;

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
        window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.musicPrimaryDark));
    }

    @Override
    protected void initData() {

    }

    @Nullable
    @Override
    protected String[] needRequestPermission() {
        return new String[0];
    }
}
