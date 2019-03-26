package com.inz.z.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.inz.z.R;
import com.inz.z.view.widget.TiwActionLayout;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/26 10:36.
 */
public class TiwBaseActivity extends AbsBaseActivity {

    private TiwActionLayout tiwActionLayout;

    @Override
    protected void initWindow() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.tiw_base_layout;
    }

    @Override
    protected void initView() {
        tiwActionLayout = findViewById(R.id.tiw_base_tal);
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean myOnKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
