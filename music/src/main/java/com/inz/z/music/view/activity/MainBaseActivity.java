package com.inz.z.music.view.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.inz.z.music.R;
import com.inz.z.music.base.AbsBaseActivity;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/04 10:49.
 */
@Route(path = "/music/mainActivity", group = "music")
public class MainBaseActivity extends AbsBaseActivity {
    @Override
    protected void initWindow() {
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.man_activity;
    }

    @Override
    protected void initView() {

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
